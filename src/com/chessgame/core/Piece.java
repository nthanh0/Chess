package com.chessgame.core;

public abstract class Piece {
    private final PieceColor color;
    private PieceType type;

    public Piece(PieceColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public PieceColor getColor() { return color; }
    public PieceType getType() { return type; }
    public void setType(PieceType type) { this.type = type; }

    public abstract boolean isValidMove(Board board, Square from, Square to);
}
