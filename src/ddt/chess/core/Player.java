package ddt.chess.core;

public class Player {
    private final PieceColor color;
    private long remainingTimeMillis;
    private final boolean isComputer;

    public Player(PieceColor color, long remainingTimeMillis, boolean isComputer) {
        this.color = color;
        this.remainingTimeMillis = remainingTimeMillis;
        this.isComputer = isComputer;
    }


    public PieceColor getColor() {
        return color;
    }

    public long getRemainingTimeMillis() {
        return remainingTimeMillis;
    }

    public boolean getIsComputer() {
        return isComputer;
    }

}
