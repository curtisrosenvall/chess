package chess.pieces;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Queen extends ChessPiece {

    public Queen(ChessGame.TeamColor color) {
        super(color, PieceType.KNIGHT);
    }
}
