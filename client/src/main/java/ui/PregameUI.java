package ui;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import facade.ServerFacade;
import models.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PregameUI {

    static ServerFacade serverFacade;
    static String authToken;

    public PregameUI() {
        serverFacade = new ServerFacade(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        
    }


}
