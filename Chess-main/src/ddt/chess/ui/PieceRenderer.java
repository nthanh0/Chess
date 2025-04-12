package ddt.chess.ui;

import ddt.chess.core.Piece;
import ddt.chess.core.PieceColor;
import ddt.chess.core.PieceType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PieceRenderer {
    private final Map<String, Image> pieceImages;
    private final Font pieceFont;

    public PieceRenderer() {
        pieceImages = new HashMap<>();
        pieceFont = new Font("Arial", Font.BOLD, 40);
    }

    public void drawPiece(Graphics2D g2d, Piece piece, int x, int y, int size) {
        g2d.setFont(pieceFont);

        if (piece.getColor() == PieceColor.WHITE) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.BLACK);
        }

        String symbol = getPieceSymbol(piece.getType());
        FontMetrics metrics = g2d.getFontMetrics(pieceFont);
        int xPos = x + (size - metrics.stringWidth(symbol)) / 2;
        int yPos = y + ((size - metrics.getHeight()) / 2) + metrics.getAscent();

        // Draw shadow for visibility
        if (piece.getColor() == PieceColor.WHITE) {
            g2d.setColor(Color.BLACK);
            g2d.drawString(symbol, xPos + 1, yPos + 1);
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.WHITE);
            g2d.drawString(symbol, xPos + 1, yPos + 1);
            g2d.setColor(Color.BLACK);
        }

        g2d.drawString(symbol, xPos, yPos);
    }

    private String getPieceSymbol(PieceType type) {
        return switch (type) {
            case PAWN -> "♙";
            case KNIGHT -> "♘";
            case BISHOP -> "♗";
            case ROOK -> "♖";
            case QUEEN -> "♕";
            case KING -> "♔";
        };
    }
}