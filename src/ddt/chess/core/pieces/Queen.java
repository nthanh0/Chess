package ddt.chess.core.pieces;
import ddt.chess.core.*;

public class Queen extends Piece {
    public Queen(PieceColor color) { super(color, PieceType.QUEEN); }

    @Override
    public boolean isValidMove(Board board, Square fromSquare, Square toSquare) {
        // temporary instances of a rook and a bishop to use their isValidMove()
        Rook tempRook = new Rook(PieceColor.WHITE);
        Bishop tempBishop = new Bishop(PieceColor.WHITE);
        // a queen is basically a rook and a bishop combined
        return (tempBishop.isValidMove(board, fromSquare, toSquare)
             || tempRook.isValidMove(board, fromSquare, toSquare));
    }
}
