package ddt.chess.pieces;
import ddt.chess.*;

public class Queen extends Piece {
    public Queen(PieceColor color) { super(color, PieceType.QUEEN); }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        // temporary instances of a rook and a bishop to use their isValidMove()
        Rook tempRook = new Rook(PieceColor.WHITE);
        Bishop tempBishop = new Bishop(PieceColor.WHITE);
        // a queen is basically a rook and a bishop combined
        return (tempBishop.isValidMove(board, from, to) || tempRook.isValidMove(board, from, to));
    }
}
