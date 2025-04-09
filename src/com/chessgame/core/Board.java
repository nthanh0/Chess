package com.chessgame.core;

public class Board {
    private Square board[][];

    public Board() {
        // create empty board
        board = new Square[8][8];
    }

    public Square getSquare(int x, int y) {
        return board[x][y];
    }

    public Square[][] getBoard() {
        return board;
    }
}
