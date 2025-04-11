package ddt.chess.util;

import ddt.chess.core.*;
import ddt.chess.logic.MoveValidator;

public class Notation {

    // from e.g "a3" to Square(5, 0);
    public static Square getSquareFromNotation(Board board, String notation) {
        int x = 7 - (notation.charAt(1) - '1');
        int y = notation.charAt(0) - 'a';
        return board.getSquare(x, y);
    }

    // the opposite
    public static String squareToNotation(Square square) {
        // empty string (for string type casting) + file char + rank char
        return "" + (char)('a' + square.getY()) + (char)('1' + (7 - square.getX()));
    }

    public static char getPieceSymbol(PieceType type) {
        return switch (type) {
            case PAWN -> 'P';
            case KNIGHT -> 'N';
            case BISHOP -> 'B';
            case ROOK -> 'R';
            case QUEEN -> 'Q';
            case KING -> 'K';
        };
    }

    public static String moveToNotation(Move move, Board board) {
        StringBuilder res = new StringBuilder();
        boolean isPawn = move.getMovingPiece().getType() == PieceType.PAWN;
        int fromSquareX = move.getFromSquare().getX();
        int fromSquareY = move.getFromSquare().getY();
        if (!isPawn) {
            res.append(Notation.getPieceSymbol(move.getMovingPiece().getType()));
        }
        // castling
        if (move.getMovingPiece().getType() == PieceType.KING
            && move.getFromSquare().yDistanceTo(move.getToSquare()) == 2) {
            return move.getToSquare().getY() > fromSquareY ? "O-O" : "O-O-O";
        }
        if (isPawn) {
            // pawns only and always have to specify the file when capturing
            // so no checking is needed
            if (move.isCapture()) {
                res.append(Notation.squareToNotation(move.getFromSquare()).charAt(0));
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (i == fromSquareX && j == fromSquareY) {
                        continue;
                    }
                    Square otherSquare = board.getSquare(i, j);
                    Piece otherPiece = otherSquare.getPiece();
                    Move otherMove = new Move(otherSquare, move.getToSquare());
                    if (otherPiece != null
                            && otherPiece.getColor() == move.getMovingPiece().getColor()
                            && otherPiece.getType() == move.getMovingPiece().getType()) {
                        if (MoveValidator.isValidMove(board, otherMove)) {
                            if (otherSquare.getX() == fromSquareX) {
                                // if there is another piece of the same type in the same rank
                                // then specify the file
                                res.append(Notation.squareToNotation(move.getFromSquare()).charAt(0));
                            }
                            if (otherSquare.getY() == fromSquareY) {
                                // if there is another piece of the same type in the same file
                                // then specify the rank
                                res.append(Notation.squareToNotation(move.getFromSquare()).charAt(1));
                            }
                            // if neither but the other piece can still move to that same square
                            // then specify the rank
                            res.append(Notation.squareToNotation(move.getFromSquare()).charAt(0));
                        }
                    }
                }
            }
        }
        if (move.isCapture()) {
            res.append('x');
        }
        res.append(Notation.squareToNotation(move.getToSquare()));
        return res.toString();
    }

}
