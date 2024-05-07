package chess;
import chess.ChessPiece.PieceType;
import java.util.Objects;

public class ChessMove {
    private ChessPosition start;
    private ChessPosition end;
    private PieceType pieceType;


    public ChessMove(ChessPosition start, ChessPosition end, PieceType pieceType) {
        this.start = start;
        this.end = end;
        this.pieceType = pieceType;
    }

    public ChessPosition getStart() {
        return start;
    }

    public ChessPosition getEnd() {
        return end;
    }


    @Override
    public String toString() {
        return "ChessMove{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end) && pieceType == chessMove.pieceType;
    }


    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
