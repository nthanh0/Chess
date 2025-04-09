package ddt.chess.pieces;
import ddt.chess.*;

public class Knight extends Piece {
    public Knight(PieceColor color) { super(color, PieceType.KNIGHT); }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        if (Math.abs(from.getX() - to.getX()) == 2) {
            // if the vertical difference is 2 squares
            // then the horizontal difference must be 1 for the move to be valid knight move
            return (Math.abs(from.getY() - to.getY()) == 1);
        } else if (Math.abs(from.getX() - to.getX()) == 1) {
            // likewise, if horizontal difference is 1
            // then the vertical difference must be 2
            return (Math.abs(from.getY() - to.getY()) == 2);
        } else {
            // if neither of the above then obviously not a knight move
            return false;
        }
    }
}
