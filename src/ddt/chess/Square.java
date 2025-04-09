package ddt.chess;

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
    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}