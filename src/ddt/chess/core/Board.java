package ddt.chess.core;

public class Board {
    private Square board[][];

    public Board() {
        // create empty board
        board = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    public Square getSquare(int x, int y) {
        return board[x][y];
    }

    public Square[][] getBoard() {
        return board;
    }
}
