package ddt.chess.core;
import ddt.chess.logic.MoveValidator;
import ddt.chess.util.MoveHistory;

public class Game {
    private final Board board;
    private final MoveHistory history;
    private Player playerWhite;
    private Player playerBlack;
    private PieceColor turn = PieceColor.WHITE;
    private boolean isOver = false;

    public Game() {
        board = new Board();
        board.setupPieces();
        history = new MoveHistory();
    }

    public void makeMove(Move move) {
        // check if piece color aligns with turn
        if (move.getMovingPiece().getColor() == turn) {
            if (MoveValidator.isValidMove(board, move)) {
                // make move and add move to history
                board.makeMove(move);
                history.addMove(move);
                // switch turns
                turn = (turn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            }
        }
    }

    public void undoLastMove() {
        if (!history.isEmpty()) {
            board.undoMove(history.getLastMove());
            history.undoLastMove();
        }
    }

    public Board getBoard() {
        return board;
    }

    public MoveHistory getHistory() {
        return history;
    }

    public PieceColor getCurrentTurn() {
        return turn;
    }

    public boolean isCheckMate() {
        // has to be in check to be a checkmate
        if (!board.isCheck(turn)) {
            return false;
        }
        // if there is no valid move then it's checkmate
        return board.generateAllValidMoves(turn).isEmpty();
    }

    public boolean isStalemate() {
        // has to not be in check to be a stalemate
        if (!board.isCheck(turn)) {
            return false;
        }
        // if there is no valid move then it's stalemate
        return board.generateAllValidMoves(turn).isEmpty();
    }

    public Player getPlayerWhite() {
        return playerWhite;
    }

    public Player getPlayerBlack() {
        return playerBlack;
    }

    public boolean isOver() {
        return (isStalemate() || isCheckMate());
    }

    public String getGameOverMessage() {
        if (isCheckMate()) {
            return "Checkmate. "  + ((turn == PieceColor.WHITE) ? "Black" : "White") + " wins";
        } else if (isStalemate()) {
            return "Stalemate. Draw.";
        }
        return "";
    }
}
