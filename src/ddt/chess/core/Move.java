package ddt.chess.core;

// a class that stores information about a move
public class Move {
    private final Square fromSquare;
    private final Square toSquare;
    private final Piece movingPiece;
    private final Piece capturedPiece;

    public Move(Square fromSquare, Square toSquare) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.movingPiece = fromSquare.getPiece();
        this.capturedPiece = toSquare.getPiece();
    }

    public Square getFromSquare() {
        return fromSquare;
    }
    public Square getToSquare() {
        return toSquare;
    }
    public Piece getMovingPiece() {
        return movingPiece;
    }
    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isCapture() {
        return (capturedPiece != null
                && movingPiece.getColor() != capturedPiece.getColor());
    }
}
