package models;

import chess.ChessGame;

/**
 * A record that represents the data associated with a chess game.
 *
 * @param gameID The unique identifier for the game.
 * @param whiteUsername The username of the player with the white pieces.
 * @param blackUsername The username of the player with the black pieces.
 * @param gameName The name of the game.
 * @param game An instance of ChessGame representing the current state of the game.
 */
public record GameData (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}