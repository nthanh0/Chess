package ddt.chess.core;
import ddt.chess.logic.MoveValidator;
import ddt.chess.ui.TerminalUI;
import ddt.chess.util.TimerClock;

public class Game {
    private Board board;
    private final MoveHistory history;
    private Player playerWhite;
    private Player playerBlack;
    private TimerClock whiteClock;
    private TimerClock blackClock;
    private PieceColor turn = PieceColor.WHITE;

    private String gameOverMessage;

    public Game() {
        board = new Board();
        board.setupPieces();
        history = new MoveHistory();
    }

    // timed game
    public Game(TimerClock whiteClock, TimerClock blackClock) {
        board = new Board();
        board.setupPieces();
        history = new MoveHistory();
        this.whiteClock = whiteClock;
        this.blackClock = blackClock;
    }

    public boolean makeMove(Move move) {
        // check if piece color aligns with turn
        if (move.getMovingPiece() != null && move.getMovingPiece().getColor() == turn) {
            if (MoveValidator.isValidCastling(board, move)) {
                move.setMoveType(MoveType.CASTLING);
                board.performCastling(move);
            } else if (MoveValidator.isValidEnPassant(move, history)) {
                // handle en passant
                move.setMoveType(MoveType.EN_PASSANT);
                board.performEnPassant(move);
            } else if (MoveValidator.isValidMove(board, move)) {
                if (MoveValidator.isValidPromotion(move)) {
                    // handle promotions
                    move.setMoveType(MoveType.PROMOTION);
                    PieceType promoteTo = TerminalUI.askForPromotion(); // call function that asks for promotion, can be changed
                    board.promotePawn(move, promoteTo);
                } else {
                    if (move.isCapture()) {
                        move.setMoveType(MoveType.CAPTURE);
                    } else {
                        move.setMoveType(MoveType.NORMAL);
                    }
                    // make move
                    board.makeMove(move);
                }
                // set hasMoved to true
                move.getMovingPiece().setHasMoved(true);
            } else {
                // is invalid move
                return false;
            }
            // add move to history
            history.addMove(move);
            // switch turns
            switchTurns();
            // is valid move
            return true;
        }
        // if wrong turn then is invalid move
        return false;
    }

    public void undoLastMove() {
        if (!history.isEmpty()) {
            // switch turns back
            switchTurns();
            Move lastMove = history.getLastMove();
            // restore hasMoved flag
            if (lastMove.isFirstMoveOfPiece()) {
                lastMove.getMovingPiece().setHasMoved(false);
            }
            switch (lastMove.getMoveType()) {
                case CASTLING -> board.undoCastling(lastMove);
                case EN_PASSANT -> board.undoEnPassant(lastMove);
                default -> board.undoMove(lastMove);
            }
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
        if (board.generateAllValidMoves(turn).isEmpty()) {
            gameOverMessage = "Checkmate. "  + ((turn == PieceColor.WHITE) ? "Black" : "White") + " wins";
            return true;
        }
        return false;
    }

    public boolean isStalemate() {
        // has to not be in check to be a stalemate
        if (board.isCheck(turn)) {
            return false;
        }
        // if there is no valid move then it's stalemate
        if (board.generateAllValidMoves(turn).isEmpty()) {
            gameOverMessage = "Stalemate. Draw.";
            return true;
        }
        return false;
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
        return gameOverMessage;
    }

    public void resetBoard() {
        history.resetHistory();
        board = new Board();
        board.setupPieces();
    }

    public void switchTurns() {
        turn = (turn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }

    public TimerClock getWhiteClock() {
        return whiteClock;
    }

    public TimerClock getBlackClock() {
        return blackClock;
    }

    public boolean isTimedGame() {
        return (whiteClock != null && blackClock != null);
    }

}
