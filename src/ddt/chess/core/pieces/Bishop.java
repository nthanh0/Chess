package ddt.chess.core.pieces;
import ddt.chess.core.*;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(Board board, Square fromSquare, Square toSquare) {
        // difference between the x's and y's must be equal toSquare be a valid bishop move
        return fromSquare.xDistanceTo(toSquare) == fromSquare.yDistanceTo(toSquare);
    }
}
