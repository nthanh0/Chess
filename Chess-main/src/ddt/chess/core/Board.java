package ddt.chess.core;
import ddt.chess.core.pieces.*;

public class Board {
    private final Square[][] board;

    public Board() {
        // create empty board
        board = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    public void setUpEmpty() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }
    public Square getSquare(int x, int y) {
        return board[x][y];
    }

    public void setupPieces() {
        for (int i = 0; i < 8; i++) {
            // setup pawn pieces
            board[1][i].setPiece(new Pawn(PieceColor.BLACK));
            board[6][i].setPiece(new Pawn(PieceColor.WHITE));
        }
        // black rooks
        board[0][0].setPiece(new Rook(PieceColor.BLACK));
        board[0][7].setPiece(new Rook(PieceColor.BLACK));
        // white rooks
        board[7][0].setPiece(new Rook(PieceColor.WHITE));
        board[7][7].setPiece(new Rook(PieceColor.WHITE));
        // black knights
        board[0][1].setPiece(new Knight(PieceColor.BLACK));
        board[0][6].setPiece(new Knight(PieceColor.BLACK));
        // white knights
        board[7][1].setPiece(new Knight(PieceColor.WHITE));
        board[7][6].setPiece(new Knight(PieceColor.WHITE));
        // black bishops
        board[0][2].setPiece(new Bishop(PieceColor.BLACK));
        board[0][5].setPiece(new Bishop(PieceColor.BLACK));
        // white bishops
        board[7][2].setPiece(new Bishop(PieceColor.WHITE));
        board[7][5].setPiece(new Bishop(PieceColor.WHITE));
        // queens
        board[7][3].setPiece(new Queen(PieceColor.WHITE));
        board[0][3].setPiece(new Queen(PieceColor.BLACK));
        // kings
        board[7][4].setPiece(new King(PieceColor.WHITE));
        board[0][4].setPiece(new King(PieceColor.BLACK));
    }

    public void makeMove(Move move) {
        // just moves the piece to a square
        move.getToSquare().setPiece(move.getMovingPiece());
        move.getFromSquare().setPiece(null);
    }

    public void undoMove(Move move) {
        // set the moving piece back to the square it moved from
        move.getFromSquare().setPiece(move.getMovingPiece());
        // put the captured piece back to where it was
        move.getToSquare().setPiece(move.getCapturedPiece());
    }

}
