package com.chessgame.core.pieces;
import com.chessgame.core.*;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        // difference between the x's and y's must be equal to be a valid bishop move
        return Math.abs(from.getX() - to.getX()) == Math.abs(from.getY() - to.getY());
    }
}
