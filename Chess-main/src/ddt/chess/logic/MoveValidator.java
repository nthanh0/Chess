package ddt.chess.logic;

import ddt.chess.core.Board;
import ddt.chess.core.Move;
import ddt.chess.core.Piece;
import ddt.chess.core.Square;

public class MoveValidator {
    public static boolean isValidMove(Board board, Move move) {
        // check if starting square is empty
        if (move.getFromSquare().isEmpty())  {
            return false;
        }
        // check if destination square is occupied by a piece of the same color
        if (move.getCapturedPiece() != null
            && move.getMovingPiece().getColor() == move.getCapturedPiece().getColor()) {
            return false;
        }
        // check if the moving pattern is valid
        if (!move.getMovingPiece().isValidPattern(move)) {
            return false;
        }
        // check if the moving path is blocked
        return !isPathBlocked(board, move);
    }
    public static boolean isPathBlocked(Board board, Move move) {
        Square fromSquare = move.getFromSquare();
        Square toSquare = move.getToSquare();
        Piece movingPiece = move.getMovingPiece();
        int xDirection = (fromSquare.getX() < toSquare.getX()) ? 1 : -1;
        int yDirection = (fromSquare.getY() < toSquare.getY()) ? 1 : -1;
        switch(movingPiece.getType()) {
            case PAWN:
                if (fromSquare.xDistanceTo(toSquare) == 2) {
                    // check if the square in between is empty
                    Square middleSquare = board.getSquare(fromSquare.getX() + xDirection, fromSquare.getY());
                    return (middleSquare.isOccupied());
                }
                break;
            case BISHOP:
                for (int i = fromSquare.getX() + xDirection, j = fromSquare.getY() + yDirection;
                     i != toSquare.getX() && j != toSquare.getY();
                     i += xDirection, j += yDirection) {

                    if (board.getSquare(i, j).isOccupied()) {
                        return true;
                    }
                }
                break;
            case ROOK:
                if (fromSquare.getX() == toSquare.getX()) {
                    for (int i = fromSquare.getY() + yDirection;
                         i != toSquare.getY(); i += yDirection) {
                        if (board.getSquare(fromSquare.getX(), i).isOccupied()) {
                            return true;
                        }
                    }
                } else if (fromSquare.getY() == toSquare.getY()) {
                    for (int i = fromSquare.getX() + xDirection;
                         i != toSquare.getX(); i += xDirection) {
                        if (board.getSquare(i, fromSquare.getY()).isOccupied()) {
                            return true;
                        }
                    }
                }
                break;
            case QUEEN:
                // bishop part
                for (int i = fromSquare.getX() + xDirection, j = fromSquare.getY() + yDirection;
                     i != toSquare.getX() && j != toSquare.getY();
                     i += xDirection, j += yDirection) {

                    if (board.getSquare(i, j).isOccupied()) {
                        return true;
                    }
                }

                // rook part
                if (fromSquare.getX() == toSquare.getX()) {
                    for (int i = fromSquare.getY() + yDirection;
                         i != toSquare.getY(); i += yDirection) {
                        if (board.getSquare(fromSquare.getX(), i).isOccupied()) {
                            return true;
                        }
                    }
                } else if (fromSquare.getY() == toSquare.getY()) {
                    for (int i = fromSquare.getX() + xDirection;
                         i != toSquare.getX(); i += xDirection) {
                        if (board.getSquare(i, fromSquare.getY()).isOccupied()) {
                            return true;
                        }
                    }
                }

        }
        return false;
    }
}
