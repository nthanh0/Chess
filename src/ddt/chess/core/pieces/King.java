package ddt.chess.core.pieces;
import ddt.chess.core.*;

public class King extends Piece {
    public King(PieceColor color) { super(color, PieceType.KING); }

    @Override
    public boolean isValidMove(Board board, Square fromSquare, Square toSquare) {
        // the king can only move 1 square in any direction
        return (fromSquare.xDistanceTo(toSquare) <= 1 && fromSquare.yDistanceTo(toSquare) <= 1);
    }
}
