package ddt.chess.pieces;
import ddt.chess.*;

public class Rook extends Piece {
    public Rook(PieceColor color) { super(color, PieceType.ROOK); }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        if (from.getX() == to.getX()) {
            // equal x's means moving horizontally
            // and in this case the move is only valid when the y's are different
            return from.getY() != to.getY();
        } else if (from.getY() == to.getY()) {
            // equal y's means moving horizontally
            // and in this case the move is only valid when the x's are different
            return from.getX() != to.getX();
        } else {
            // if neither the x's nor the y's are equal, then the move is obviously not valid rook move
            return false;
        }
    }
}
