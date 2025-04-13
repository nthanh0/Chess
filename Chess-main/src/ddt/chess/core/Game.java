package ddt.chess.core;
import ddt.chess.logic.MoveValidator;
import ddt.chess.util.MoveHistory;

public class Game {
    private final Board board;
    private final MoveHistory history;
    private Player playerWhite;
    private Player playerBlack;
    private PieceColor turn = PieceColor.WHITE;

    public Game() {
        board = new Board();
        board.setupPieces();
        history = new MoveHistory();
    }

    public boolean makeMove(Move move) {
        if (MoveValidator.isValidMove(board, move)) {
            board.makeMove(move);
            history.addMove(move);
            turn = (turn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            return true; // Trả về true khi nước đi hợp lệ
        }
        return false;
    }

    public void undoLastMove() {
        if (!history.isEmpty()) {
            board.undoMove(history.getLastMove());
            history.undoLastMove();
            // Đổi lượt chơi
            turn = (turn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
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

}
