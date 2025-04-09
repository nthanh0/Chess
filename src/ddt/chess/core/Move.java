package ddt.chess.core;

public class Move {
    private final Square fromSquare;
    private final Square toSquare;
    private final Piece movingPiece;
    private final Piece capturedPiece;

    Move(Square fromSquare, Square toSquare, Piece movingPiece, Piece capturedPiece) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.movingPiece = movingPiece;
        this.capturedPiece = capturedPiece;
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
}
