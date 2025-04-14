package ddt.chess.ui;

import ddt.chess.core.*;
import ddt.chess.core.MoveHistory;
import ddt.chess.util.Notation;


import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

            String startString = scanner.nextLine().toLowerCase().trim();
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

            String destString = scanner.nextLine().toLowerCase().trim();
            Square toSquare = Notation.getSquareFromNotation(board, destString);
            Move move = new Move(fromSquare, toSquare);
            boolean isValidMove = game.makeMove(move);
            String filePath = "";
            if (isValidMove) {
                if (board.isCheck(game.getCurrentTurn())) {
                    filePath = "resources/sound/move-check.wav";
                } else if (move.getMoveType() == MoveType.PROMOTION) {
                    filePath = "resources/sound/promote.wav";
                } else {
                    if (move.isCapture()) {
                        filePath = "resources/sound/capture.wav";
                    } else {
                        filePath = "resources/sound/move.wav";
                    }
                }
            } else {
                filePath = "resources/sound/illegal.wav";
            }
            File file = new File(filePath);
            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }
            catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
            catch (UnsupportedAudioFileException e) {
                System.out.println("Unsupported audio file");
            }
            catch (IOException e) {
                System.out.println("Something went wrong");
            }
            catch (LineUnavailableException e) {
                System.out.println("Unable to access audio resource");
            }
        }

        printBoard(board);
        String filePath = "resources/sound/game-end.wav";
        File file = new File(filePath);
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file");
        }
        catch (IOException e) {
            System.out.println("Something went wrong");
        }
        catch (LineUnavailableException e) {
            System.out.println("Unable to access audio resource");
        }
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
                    char pieceChar = Notation.getPieceSymbolFromPiece(piece);
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
