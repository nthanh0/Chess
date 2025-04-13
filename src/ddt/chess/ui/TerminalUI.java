package ddt.chess.ui;

import ddt.chess.core.*;
import ddt.chess.core.MoveHistory;
import ddt.chess.util.Notation;


import java.io.InputStream;
import java.util.Scanner;

public class TerminalUI {
    private final Scanner scanner;

    public TerminalUI(InputStream in) {
        this.scanner = new Scanner(in);
    }

    public void run() {
        Game game = new Game();
        Board board = game.getBoard();
        MoveHistory history = game.getHistory();

        while (!game.isOver()) {
            printHistory(board, history);
            printBoard(board);
            System.out.print("Enter starting square: ");

            // Add check for scanner.hasNextLine() to prevent NoSuchElementException
            if (!scanner.hasNextLine()) {
                System.out.println("No more input available. Exiting game.");
                break;
            }

            String startString = scanner.nextLine().trim();
            if (startString.equals("undo")) {
                game.undoLastMove();
                continue;
            }
            if (startString.equals("reset")) {
                game.resetBoard();
                continue;
            }
            if (startString.equals("quit") || startString.equals("exit")) {
                System.out.println("Game terminated by user.");
                break;
            }

            Square fromSquare = Notation.getSquareFromNotation(board, startString);

            System.out.print("Enter destination square: ");
            // Add another check here
            if (!scanner.hasNextLine()) {
                System.out.println("No more input available. Exiting game.");
                break;
            }

            String destString = scanner.nextLine().trim();
            Square toSquare = Notation.getSquareFromNotation(board, destString);
            game.makeMove(new Move(fromSquare, toSquare));
        }

        printBoard(board);
        System.out.println(game.isOver() ? game.getGameOverMessage() : "Game terminated early.");
    }

    public void printBoard(Board board) {
        for (int i = 0; i < 8; i++) {
            System.out.print(8 - i);
            System.out.print(' ');
            for (int j = 0; j < 8; j++) {
                Square square = board.getSquare(i, j);
                if (square.isEmpty()) {
                    System.out.print(". "); // Use a consistent placeholder for empty squares
                } else {
                    Piece piece = square.getPiece();
                    char pieceChar = ' ';
                    switch(piece.getType()) {
                        case PAWN -> pieceChar = 'p';
                        case QUEEN -> pieceChar = 'q';
                        case KING -> pieceChar = 'k';
                        case KNIGHT -> pieceChar = 'n';
                        case BISHOP -> pieceChar = 'b';
                        case ROOK -> pieceChar = 'r';
                    }
                    if (piece.getColor() == PieceColor.WHITE) {
                        pieceChar = Character.toUpperCase(pieceChar);
                    }
                    System.out.print(pieceChar + " ");
                }
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public void printHistory(Board board, MoveHistory history) {
        System.out.println("History: " + history.getHistoryString(board));
    }

    public static PieceType askForPromotion() {
        char choice;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose piece to promote to: Bishop(b), Knight(k), Rook(r), Queen(q): ");
        choice = Character.toUpperCase(scanner.next().charAt(0));
        return Notation.getPieceTypeFromSymbol(choice);
    }





}
