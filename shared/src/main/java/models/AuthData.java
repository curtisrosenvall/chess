package models;

/**
 * The AuthData record represents the authentication data for a user.
 * It contains an authentication token and the associated username.
 */
public record AuthData(String authToken, String username) {
    // The authToken field stores the authentication token for the user.
    // This token is used to verify the user's identity in the system.

    // The username field stores the username of the user.
    // This is the unique identifier for the user in the system.
}