package chess.pieces;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class King extends ChessPiece {

    public King(ChessGame.TeamColor color) {
        super(color, PieceType.KNIGHT);
    }
}
