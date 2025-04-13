package ddt.chess.logic;

import ddt.chess.core.*;
import ddt.chess.core.MoveHistory;

import java.util.ArrayList;

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
        if (isPathBlocked(board, move)) {
            return false;
        }
        // check if move is safe (does not introduce check)
        return board.isSafeAfterMove(move);
    }

    public static boolean isPathBlocked(Board board, Move move) {
        Square fromSquare = move.getFromSquare();
        Square toSquare = move.getToSquare();
        Piece movingPiece = move.getMovingPiece();
        int xDirection = Integer.compare(toSquare.getX(), fromSquare.getX());
        int yDirection = Integer.compare(toSquare.getY(), fromSquare.getY()); // -1, 0, or 1
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
                     i != toSquare.getX();
                     i += xDirection, j += yDirection) {

                    if (board.getSquare(i, j).isOccupied()) {
                        return true;
                    }
                }
                break;
            case ROOK:
                if (xDirection == 0) {
                    for (int i = fromSquare.getY() + yDirection;
                         i != toSquare.getY(); i += yDirection) {
                        if (board.getSquare(fromSquare.getX(), i).isOccupied()) {
                            return true;
                        }
                    }
                } else if (yDirection == 0) {
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
                     i != toSquare.getX();
                     i += xDirection, j += yDirection) {

                    if (board.getSquare(i, j).isOccupied()) {
                        return true;
                    }
                }

                // rook part
                if (xDirection == 0) {
                    for (int i = fromSquare.getY() + yDirection;
                         i != toSquare.getY(); i += yDirection) {
                        if (board.getSquare(fromSquare.getX(), i).isOccupied()) {
                            return true;
                        }
                    }
                } else if (yDirection == 0) {
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

    public static boolean isDoublePawnPush(Move move) {
        if (move.getMovingPiece().getType() != PieceType.PAWN) {
            return false;
        }
        return move.getFromSquare().xDistanceTo(move.getToSquare()) == 2;
    }

    public static boolean isValidPromotion(Move move) {
        if (move.getMovingPiece().getType() != PieceType.PAWN) {
            return false;
        }
        int promotionRank = (move.getMovingPiece().getColor() == PieceColor.WHITE) ? 0 : 7;
        // pawn has to be in the last rank to promote
        if (!(move.getToSquare().getX() == promotionRank)) {
            return false;
        }
        return true;
    }

    public static boolean isValidEnPassant(Move move, MoveHistory history) {
        // check if moving piece is a pawn
        if (move.getMovingPiece().getType() != PieceType.PAWN) {
            return false;
        }
        // check if move is the first move of the game
        if (history.isEmpty()) {
            return false;
        }
        Move lastMove = history.getLastMove();
        // check if last move is a double pawn push
        if (!MoveValidator.isDoublePawnPush(lastMove)) {
            return false;
        }
        int xDirection = (move.getMovingPiece().isWhite()) ? -1 : 1;
        // check if 2 pawns are in the same rank
        if (move.getFromSquare().getX() != lastMove.getToSquare().getX()) {
            return false;
        }
        // check if capture pattern is correct
        // check if destination is 1 rank above the opponent's pawn
        if (!(move.getToSquare().getX() - lastMove.getToSquare().getX() == xDirection)) {
            return false;
        }
        // check if pawn is on the same file as the opponent's captured pawn
        if (!(move.getToSquare().getY() == lastMove.getToSquare().getY())) {
            return false;
        }
        return true;
    }

    public static boolean isValidCastling(Board board, Move move) {
        //  ignore empty from squares
        if (move.getFromSquare().isEmpty()) {
            return false;
        }
        // ignore non-king pieces
        if (move.getMovingPiece().getType() != PieceType.KING) {
            return false;
        }
        // check if king has moved
        if ((move.getMovingPiece().hasMoved())) {
            System.out.println("KING HAS MOVED");
            return false;
        }
        // check if king is in check
        if (board.isCheck(move.getMovingPiece().getColor())) {
            System.out.println("KING IS IN CHECK");
            return false;
        }

        int fromX = move.getFromSquare().getX();
        int fromY = move.getFromSquare().getY();
        int toX = move.getToSquare().getX();
        int toY = move.getToSquare().getY();

        // check if king is in the first rank
        if (move.getMovingPiece().isWhite()) {
            if (!(fromX == 7 && toX == 7)) {
                return false;
            }
        } else {
            if (!(fromX == 0 && toX == 0)) {
                return false;
            }
        }

        boolean isShortCastle = (fromY == 4 && toY == 6);
        boolean isLongCastle = (fromY == 4 && toY == 2);
        if (!isShortCastle && !isLongCastle) {
            System.out.println("NOT A CASTLE");
            return false;
        }

        // check if king is in check after move
        if (!board.isSafeAfterMove(move)) {
            System.out.println("NOT SAFE");
            return false;
        }

        int rookY;
        if (isShortCastle) {
            rookY = 7;
        } else {
            rookY = 0;
        }
        Square originalRookSquare = board.getSquare(fromX, rookY);
        Move rookToKing = new Move(originalRookSquare, move.getFromSquare());
        // check if square is empty, or piece has moved, or path between the rook and king is blocked
        if (originalRookSquare.isEmpty()
            || originalRookSquare.getPiece().hasMoved()
            || isPathBlocked(board, rookToKing)) {
            System.out.println("ROOK SQUARE IS NOT INACT");
            return false;
        }

        // check if king does not pass through a square that is under attack
        Square squareToCheck;
        if (isShortCastle) {
            squareToCheck = board.getSquare(fromX, 5);
        } else {
            squareToCheck = board.getSquare(fromX, 3);
        }
        PieceColor opponentColor = (move.getMovingPiece().isWhite()) ? PieceColor.BLACK : PieceColor.WHITE;
        ArrayList<Move> validMoves = board.generateAllValidMoves(opponentColor);
        for (Move validMove : validMoves) {
            if (validMove.getToSquare().equals(squareToCheck)) {
                System.out.println("KING PASSES THROUGH UNSAFE SQUARE");
                return false;
            }
        }

        return true;
    }
}
