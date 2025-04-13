package ddt.chess.core;
import ddt.chess.util.Notation;

import java.util.ArrayList;

public class MoveHistory {
    private final ArrayList<Move> history;

    public MoveHistory() {
        history = new ArrayList<>();
    }

    public void addMove(Move move) {
        history.add(move);
    }

    public void undoLastMove() {
        if (!history.isEmpty()) {
            history.removeLast();
        }
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public Move getLastMove() {
        return history.getLast();
    }

    public String getHistoryString(Board board) {
        StringBuilder res = new StringBuilder();
        // move index
        int moveIndex = 1;
        for (int i = 0; i < history.size(); i++) {
            // show and increase move index every 2 moves
            // because a recorded move contains moves from both sides
            if (i % 2 == 0) {
                res.append(moveIndex).append(". ");
                moveIndex++;
            }
            Move move = history.get(i);
            res.append(Notation.moveToNotation(move, board)).append(' ');
        }
        return res.toString();
    }
    public void resetHistory() {
        history.clear();
    }

}
