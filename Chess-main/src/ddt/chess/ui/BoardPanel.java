package ddt.chess.ui;

import ddt.chess.core.*;
import ddt.chess.util.ImageLoader;
import ddt.chess.util.Notation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
    private final Game game;
    private final ImageLoader imageLoader;
    private Square selectedSquare;
    private final int SQUARE_SIZE = 60;
    private GameControlPanel controlPanel;
    private boolean kingIsInCheck = false;
    private Square checkedKingSquare = null;

    public BoardPanel(Game game, ImageLoader imageLoader) {
        this.game = game;
        this.imageLoader = imageLoader;
        this.setPreferredSize(new Dimension(SQUARE_SIZE * 8, SQUARE_SIZE * 8));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleSquareClick(e.getX() / SQUARE_SIZE, e.getY() / SQUARE_SIZE);
            }
        });

        // Kiểm tra trạng thái chiếu vua ban đầu
        updateCheckStatus();
    }

    private void handleSquareClick(int y, int x) {
        Board board = game.getBoard();
        Square clickedSquare = board.getSquare(x, y);

        if (selectedSquare == null) {
            if (!clickedSquare.isEmpty() &&
                    clickedSquare.getPiece().getColor() == getCurrentPlayerColor()) {
                selectedSquare = clickedSquare;
                repaint();
            }
        } else {
            // If clicked on same square, deselect
            if (selectedSquare == clickedSquare) {
                selectedSquare = null;
                repaint();
                return;
            }

            // Attempt to make a move
            Move move = new Move(selectedSquare, clickedSquare);
            boolean moveSuccess = game.makeMove(move);
            if (moveSuccess) {
                // Cập nhật bảng hiển thị lịch sử nước đi
                if (controlPanel != null) {
                    controlPanel.updateMoveHistory();
                }
                // Cập nhật trạng thái chiếu vua
                updateCheckStatus();
            }
            selectedSquare = null;
            repaint();
        }
    }

    private void updateCheckStatus() {
        PieceColor currentColor = game.getCurrentTurn();
        // Kiểm tra nếu vua của người chơi hiện tại đang bị chiếu
        if (game.getBoard().isCheck(currentColor)) {
            kingIsInCheck = true;
            checkedKingSquare = game.getBoard().findKingSquare(currentColor);
        } else {
            kingIsInCheck = false;
            checkedKingSquare = null;
        }
    }


    private PieceColor getCurrentPlayerColor() {
        return game.getCurrentTurn();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Use themed squares from ImageLoader
                if ((i + j) % 2 == 0) {
                    // Light squares
                    imageLoader.getLightSquareImage().paintIcon(this, g2d, j * SQUARE_SIZE, i * SQUARE_SIZE);
                } else {
                    // Dark squares
                    imageLoader.getDarkSquareImage().paintIcon(this, g2d, j * SQUARE_SIZE, i * SQUARE_SIZE);
                }

                // Highlight selected square
                if (selectedSquare != null &&
                        selectedSquare.getX() == i &&
                        selectedSquare.getY() == j) {
                    imageLoader.getSelectedSquareImage().paintIcon(this, g2d, j * SQUARE_SIZE, i * SQUARE_SIZE);
                }

                // Highlight king in check
                if (kingIsInCheck && checkedKingSquare != null &&
                        checkedKingSquare.getX() == i &&
                        checkedKingSquare.getY() == j) {
                    imageLoader.getCheckSquareImage().paintIcon(this, g2d, j * SQUARE_SIZE, i * SQUARE_SIZE);
                }
            }
        }

        // Draw the pieces
        Board board = game.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = board.getSquare(i, j);
                if (!square.isEmpty()) {
                    drawPiece(g2d, square.getPiece(), j * SQUARE_SIZE, i * SQUARE_SIZE);
                }
            }
        }

        // Draw coordinates
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        for (int i = 0; i < 8; i++) {
            // File labels (a-h)
            g2d.drawString(String.valueOf((char)('a' + i)),
                    i * SQUARE_SIZE + SQUARE_SIZE - 12,
                    8 * SQUARE_SIZE - 2);
            // Rank labels (1-8)
            g2d.drawString(String.valueOf(8 - i),
                    2,
                    i * SQUARE_SIZE + 12);
        }
    }

    private void drawPiece(Graphics2D g2d, Piece piece, int x, int y) {
        String pieceCode = getPieceCode(piece);
        ImageIcon pieceImage = imageLoader.getPieceImage(pieceCode);
        if (pieceImage != null) {
            pieceImage.paintIcon(this, g2d, x + (SQUARE_SIZE - pieceImage.getIconWidth()) / 2,
                    y + (SQUARE_SIZE - pieceImage.getIconHeight()) / 2);
        }
    }

    private String getPieceCode(Piece piece) {
        // Convert piece to code format expected by ImageLoader
        char code = ' ';
        switch (piece.getType()) {
            case PAWN -> code = 'P';
            case ROOK -> code = 'R';
            case KNIGHT -> code = 'N';
            case BISHOP -> code = 'B';
            case QUEEN -> code = 'Q';
            case KING -> code = 'K';
        }

        return piece.getColor() == PieceColor.WHITE ?
                String.valueOf(code) :
                String.valueOf(code).toLowerCase();
    }

    public void resetBoard() {
        selectedSquare = null;
        kingIsInCheck = false;
        checkedKingSquare = null;
        repaint();
    }

    public void setControlPanel(GameControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }
}