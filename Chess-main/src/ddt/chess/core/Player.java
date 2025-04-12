package ddt.chess.core;

public class Player {
    private final PieceColor color;
    private long remainingTimeMillis;
    private int score;

    public Player(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public long getRemainingTimeMillis() {
        return remainingTimeMillis;
    }
}
