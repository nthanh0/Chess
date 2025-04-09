package ddt.chess.core.pieces;
import ddt.chess.core.*;

public class Pawn extends Piece {
    public Pawn(PieceColor color) { super(color, PieceType.PAWN); }

    @Override
    public boolean isValidMove(Board board, Square fromSquare, Square toSquare) {
        boolean isCapture = (toSquare.getPiece() != null);
        // xDistance is not absolute because the pawn's capture direction depends on the color
        // and so can be positive or negative
        int xDistance = toSquare.getX() - fromSquare.getX();
        // adjust xDistance for direction
        if (toSquare.getPiece().isWhite()) {
            xDistance *= -1;
        }
        int yDistance = fromSquare.yDistanceTo(toSquare);

        if (isCapture) {
            return (yDistance == 1 && xDistance == 1);
        } else {
            // moving 1 or 2 squares away from starting file (second or seventh)
            if (yDistance == 0) {
                if (xDistance == 1) {
                    return true;
                }
                if (xDistance == 2) {
                    if (fromSquare.getPiece().isWhite()) {
                        return (fromSquare.getX() == 6);
                    } else {
                        return (fromSquare.getX() == 1);
                    }
                 }
            }
        }
        return false;
    }

}
