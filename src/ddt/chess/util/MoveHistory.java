package ddt.chess.util;

import ddt.chess.core.Board;
import ddt.chess.core.Move;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
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
        for (Move move : history) {
            res.append(Notation.moveToNotation(move, board)).append(" ");
        }
        return res.toString();
    }

    public void saveHistoryToFile(Board board) {
        File file = createNewGameFile();
        if (file == null) {
            System.out.println("Không thể tạo file mới");
            return;
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(getHistoryString(board));
            System.out.println("Lưu lịch sử thành công vào file: " + file.getName());
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private File createNewGameFile() {
        createFolder();
        int count = 1;
        File file;
        while (true) {
            file = new File("History/game" + count + ".txt");
            if (!file.exists()) {
                break;
            }
            count++;
        }
        try {
            if (file.createNewFile()) {
                System.out.println("Đã tạo file: " + file.getName());
                return file;
            } else {
                System.out.println("Không thể tạo file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createFolder() {
        File folder = new File("History");
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Đã tạo thư mục");
            } else {
                System.out.println("Không thể tạo thư mục");
            }
        }
    }

    public void NewGameEvent(Board board) {
        if (!history.isEmpty()) {
            saveHistoryToFile(board);
            System.out.println("Đã lưu lịch sử game vào file.");
        }

        createNewGameFile();
        System.out.println("Đã tạo file mới cho game tiếp theo.");
    }
}
