package ddt.chess.core;
import ddt.chess.core.pieces.*;
import ddt.chess.logic.MoveValidator;

import java.util.ArrayList;

public class Board {
    private final Square[][] board;

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

    public void setupPieces() {
        for (int i = 0; i < 8; i++) {
            // setup pawn pieces
            board[1][i].setPiece(new Pawn(PieceColor.BLACK));
            board[6][i].setPiece(new Pawn(PieceColor.WHITE));
        }
        // black rooks
        board[0][0].setPiece(new Rook(PieceColor.BLACK));
        board[0][7].setPiece(new Rook(PieceColor.BLACK));
        // white rooks
        board[7][0].setPiece(new Rook(PieceColor.WHITE));
        board[7][7].setPiece(new Rook(PieceColor.WHITE));
        // black knights
        board[0][1].setPiece(new Knight(PieceColor.BLACK));
        board[0][6].setPiece(new Knight(PieceColor.BLACK));
        // white knights
        board[7][1].setPiece(new Knight(PieceColor.WHITE));
        board[7][6].setPiece(new Knight(PieceColor.WHITE));
        // black bishops
        board[0][2].setPiece(new Bishop(PieceColor.BLACK));
        board[0][5].setPiece(new Bishop(PieceColor.BLACK));
        // white bishops
        board[7][2].setPiece(new Bishop(PieceColor.WHITE));
        board[7][5].setPiece(new Bishop(PieceColor.WHITE));
        // queens
        board[7][3].setPiece(new Queen(PieceColor.WHITE));
        board[0][3].setPiece(new Queen(PieceColor.BLACK));
        // kings
        board[7][4].setPiece(new King(PieceColor.WHITE));
        board[0][4].setPiece(new King(PieceColor.BLACK));
    }

    public void makeMove(Move move) {
        // just moves the piece to a square
        move.getToSquare().setPiece(move.getMovingPiece());
        move.getFromSquare().setPiece(null);
    }

    public void undoMove(Move move) {
        // set the moving piece back to the square it moved from
        move.getFromSquare().setPiece(move.getMovingPiece());
        // put the captured piece back to where it was
        move.getToSquare().setPiece(move.getCapturedPiece());
    }


    public Square findKingSquare(PieceColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = board[i][j];
                if (square.isEmpty()) {
                    // skip empty squares;
                    continue;
                }
                Piece piece = square.getPiece();
                if (piece.getType() == PieceType.KING
                        && piece.getColor() == color) {
                    return square;
                }
            }
        }
        return null;
    }


    public boolean isCheck(PieceColor color) {
        Square kingSquare = findKingSquare(color);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = board[i][j];
                if (square.isEmpty()) {
                    // skip empty squares;
                    continue;
                }
                Piece piece = square.getPiece();
                if (piece.getColor() == color) {
                    // skip pieces of the same color
                    continue;
                }
                Move moveToKing = new Move(square, kingSquare);
                if (MoveValidator.isValidMove(this, moveToKing)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Move> generateAllValidMoves(PieceColor color) {
        ArrayList<Move> res = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = getSquare(i, j);
                if (square.isEmpty()) {
                    // skip empty squares;
                    continue;
                }
                if (square.getPiece().getColor() != color) {
                    // ignore pieces of the opposite color
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        Square otherSquare = getSquare(k, l);
                        Move move = new Move(square, otherSquare);
                        if (MoveValidator.isValidMove(this, new Move(square, otherSquare))) {
                            res.add(move);
                        }
                    }
                }
            }
        }
        return res;
    }

    // Modify the isSafeAfterMove method in Board.java
    public boolean isSafeAfterMove(Move move) {
        boolean res;
        // Remember the original piece at the destination
        Piece originalPiece = move.getToSquare().getPiece();

        // Make the move
        makeMove(move);

        // Check if the player's own king is in check after the move
        res = !isCheck(move.getMovingPiece().getColor());

        // Undo the move
        move.getFromSquare().setPiece(move.getMovingPiece());
        move.getToSquare().setPiece(originalPiece);

        return res;
    }

    public void setUpEmpty() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }
}