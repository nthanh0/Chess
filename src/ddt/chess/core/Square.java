package ddt.chess.core;

public class Square {
    private final int x;
    private final int y;
    Piece piece;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Square(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int xDistanceTo(Square other) {
        return Math.abs(x - other.getX());
    }

    public int yDistanceTo(Square other) {
        return Math.abs(y - other.getY());
    }

    // from e.g "a3" to Square(5, 0);
    public static Square fromNotation(String notation) {
        int x = notation.charAt(1) - '1';
        int y = notation.charAt(0) - 'a';
        return new Square(x, y);
    }

    // the opposite
    public String toNotation() {
        // empty string (for string type casting) + rank char + file char
        return "" + (char)('a' + y) + (char)('1' + x);
    }
}