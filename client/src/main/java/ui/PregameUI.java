package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PregameUI {


    public PregameUI(){

    }

    public void playChess() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        System.out.print(ERASE_SCREEN);
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print("Welcome to Curt's Chess Server. Type \"help\" to starting playing");
        System.out.print(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
        preLoginUI(out);
    }

    public void preLoginUI(PrintStream out) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.print("[LOGGED OUT] >>> ");
            String inputLine = scanner.nextLine().trim();
            String[] tokens = inputLine.split("\\s+");
            String command = tokens[0].toLowerCase();

            switch (command) {
                case "quit":
                    isRunning = false;
                    break;

                case "help":
                    displayHelp(out);
                    break;

                case "login":
                    handleLogin(out)
                    break;

                case "register":
                    displayHelp(out);
                    break;

                default:
                    displayHelp(out);
                    break;
            }
        }
    }

    private void displayHelp(PrintStream out) {
        write(out, "register <USERNAME> <PASSWORD> <EMAIL>", "to create an account");
        write(out, "login <USERNAME> <PASSWORD>", "to play chess");
        write(out, "quit", "exit the application");
        write(out, "help", "display available commands");
    }

    private void write(PrintStream out, String message, String explanation) {

    }

    private void handleLogin(String username, String password) {

    }

    private handleLogout(String authToken){
        
    }
}
