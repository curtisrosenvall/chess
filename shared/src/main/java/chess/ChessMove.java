package chess;

import java.util.Objects;

public class ChessMove {
    private ChessPosition start;
    private ChessPosition end;
    private ChessPiece.PieceType pieceType;  // Add piece type

    public ChessMove(ChessPosition start, ChessPosition end, ChessPiece.PieceType pieceType) {
        this.start = start;
        this.end = end;
        this.pieceType = pieceType;  // Store the piece type
    }

    public ChessPosition getStart() {
        return start;
    }

    public ChessPosition getEnd() {
        return end;
    }

    public ChessPiece.PieceType getPieceType() {
        return pieceType;  // Getter for piece type
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
        return Objects.hash(start, end, pieceType);
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "start=" + start +
                ", end=" + end +
                ", pieceType=" + pieceType +
                '}';
    }
}
