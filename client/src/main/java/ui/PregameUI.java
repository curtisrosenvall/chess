package ui;


import chess.ChessBoard;
import chess.ChessGame;
import facade.ServerFacade;
import models.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PregameUI {

    private ServerFacade facade;

    public PregameUI() {
        facade = new ServerFacade(8080);
    }

    public void beginJourney() {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("Welcome to the C S 240 chess server. Type \"help\" to begin.");
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        preLoginUI(out);
    }

    public void preLoginUI(PrintStream out) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            out.print(SET_TEXT_COLOR_RED);
            out.print("[LOGGED OUT] >>> ");
            String inputLine = scanner.nextLine().trim();
            String[] tokens = inputLine.split("\\s+");
            String command = tokens[0].toLowerCase();

            switch (command) {
                case "quit":
                    isRunning = false;
                    break;

                case "help":
                    displayPreLoginHelp(out);
                    break;

                case "login":
                    handleLogin(out, tokens);
                    break;

                case "register":
                    handleRegister(out, tokens);
                    break;

                default:
                    pleaseTryAgain(out);
                    break;
            }
        }
    }

    private void displayPreLoginHelp(PrintStream out) {
        tableWriter(out, "register <USERNAME> <PASSWORD> <EMAIL>", "to create an account");
        tableWriter(out, "login <USERNAME> <PASSWORD>", "to play chess");
        tableWriter(out, "quit", "exit the application");
        tableWriter(out, "help", "display available commands");
    }

    private void handleLogin(PrintStream out, String[] tokens) {
        if (tokens.length < 3) {
            out.println("Usage: login <USERNAME> <PASSWORD>");
            return;
        }

        String username = tokens[1];
        String password = tokens[2];

        out.print(SET_TEXT_COLOR_WHITE);
        out.println("  Username: " + username);
        out.println("  Password: " + password);

        try {
            String authToken = facade.login(username, password);
            postLoginUI(out, authToken);
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    private void handleRegister(PrintStream out, String[] tokens) {
        if (tokens.length < 4) {
            out.println("Usage: register <USERNAME> <PASSWORD> <EMAIL>");
            return;
        }

        String username = tokens[1];
        String password = tokens[2];
        String email = tokens[3];

        out.print(SET_TEXT_COLOR_WHITE);
        out.println("  Username: " + username);
        out.println("  Password: " + password);
        out.println("  Email: " + email);

        try {
            String authToken = facade.register(username, password, email);
            postLoginUI(out, authToken);
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    public void postLoginUI(PrintStream out, String authToken) {
        Scanner scanner = new Scanner(System.in);
        boolean isLoggedIn = true;

        while (isLoggedIn) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("[LOGGED IN] >>> ");
            String inputLine = scanner.nextLine().trim();
            String[] tokens = inputLine.split("\\s+");
            String command = tokens[0].toLowerCase();

            switch (command) {
                case "quit":
                    isLoggedIn = false;
                    break;

                case "help":
                    displayPostLoginHelp(out);
                    break;

                case "logout":
                    handleLogout(out, authToken);
                    isLoggedIn = false;
                    break;

                case "create":
                    handleCreateGame(out, tokens, authToken);
                    break;

                case "list":
                    handleListGames(out, authToken);
                    break;

                case "join":
                    handleJoinGame(out, tokens, authToken);
                    break;

                case "observe":
                    handleObserveGame(out, tokens, authToken);
                    break;

                default:
                    pleaseTryAgain(out);
                    break;
            }
        }
    }

    private void displayPostLoginHelp(PrintStream out) {
        tableWriter(out, "create <NAME>", "create a game");
        tableWriter(out, "list", "list available games");
        tableWriter(out, "join <ID> [WHITE|BLACK]", "join a game");
        tableWriter(out, "observe <ID>", "observe a game");
        tableWriter(out, "logout", "log out of your account");
        tableWriter(out, "quit", "exit the application");
        tableWriter(out, "help", "display available commands");
    }

    private void handleLogout(PrintStream out, String authToken) {
        try {
            facade.logout(authToken);
            out.println("Successfully logged out.");
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    private void handleCreateGame(PrintStream out, String[] tokens, String authToken) {
        if (tokens.length < 2) {
            out.println("Usage: create <NAME>");
            return;
        }

        String gameName = tokens[1];

        try {
            facade.createGame(authToken, gameName);
            out.println("Game \"" + gameName + "\" created successfully.");
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    private void handleListGames(PrintStream out, String authToken) {
        out.print(SET_TEXT_COLOR_WHITE);
        try {
            ArrayList<GameData> games = facade.listGames(authToken);
            for (GameData game : games) {
                out.println("  " + game);
            }
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    private void handleJoinGame(PrintStream out, String[] tokens, String authToken) {
        if (tokens.length < 3) {
            out.println("Usage: join <ID> [WHITE|BLACK]");
            return;
        }

        try {
            int gameID = Integer.parseInt(tokens[1]);
            String teamColor = tokens[2].toUpperCase();

            if (teamColor.equals("WHITE") || teamColor.equals("BLACK")) {
                ChessGame.TeamColor playerColor = ChessGame.TeamColor.valueOf(teamColor);
                facade.joinGame(authToken, playerColor, gameID);

                ChessBoard myBoard = new ChessBoard();
                myBoard.resetBoard();
                out.println();
                BoardUI.printBoard(out, true, myBoard);
                out.println();
                BoardUI.printBoard(out, false, myBoard);
            } else {
                out.println("Invalid team color. Please choose WHITE or BLACK.");
            }
        } catch (NumberFormatException e) {
            out.println("Invalid game ID. Please enter a numeric value.");
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    private void handleObserveGame(PrintStream out, String[] tokens, String authToken) {
        if (tokens.length < 2) {
            out.println("Usage: observe <ID>");
            return;
        }

        try {
            int gameID = Integer.parseInt(tokens[1]);
            // Implement observation logic here
            out.println("Observing game ID: " + gameID);
        } catch (NumberFormatException e) {
            out.println("Invalid game ID. Please enter a numeric value.");
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    private void pleaseTryAgain(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("Please try again. Type \"help\" if you need assistance.");
        out.print(RESET_BG_COLOR);
    }

    private void tableWriter(PrintStream out, String message, String explanation) {
        out.print("  ");
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print(message);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println(" --> " + explanation);
    }
}
