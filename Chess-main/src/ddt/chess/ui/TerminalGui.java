package ddt.chess.ui;

import ddt.chess.core.*;
import ddt.chess.util.MoveHistory;
import ddt.chess.util.Notation;

import java.util.Scanner;

public class TerminalGui {

    public TerminalGui() {
        Game game = new Game();
        Board board = game.getBoard();
        MoveHistory history = game.getHistory();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printHistory(board, history);
            printBoard(board);
            System.out.print("Enter starting square: ");
            String startString = scanner.nextLine().trim();
            if (startString.equals("undo")) {
                game.undoLastMove();
                continue;
            }
//            if (startString.equals("reset")) {
//                board.setupPieces();
//            }
            Square fromSquare = Notation.getSquareFromNotation(board, startString);
            System.out.print("Enter destination square: ");
            String destString = scanner.nextLine();
            Square toSquare = Notation.getSquareFromNotation(board, destString);
            game.makeMove(new Move(fromSquare, toSquare));

        }
    }

    public void printBoard(Board board) {
        for (int i = 0; i < 8; i++) {
            System.out.print(8 - i);
            System.out.print(' ');
            for (int j = 0; j < 8; j++) {
                Square square = board.getSquare(i, j);
                char output = ' ';
                if (square.getPiece() != null) {
                    switch(square.getPiece().getType()) {
                        case PAWN -> output = 'p';
                        case QUEEN -> output = 'q';
                        case KING -> output = 'k';
                        case KNIGHT -> output = 'n';
                        case BISHOP -> output = 'b';
                        case ROOK -> output = 'r';
                    }
                    if (square.getPiece().getColor() == PieceColor.WHITE) {
                        output = Character.toUpperCase(output);
                    } else {
                        output = Character.toLowerCase(output);
                    }
                }
                System.out.print(output);
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public void printHistory(Board board, MoveHistory history) {
        System.out.println("History: " + history.getHistoryString(board));
    }


}
