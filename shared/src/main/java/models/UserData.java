package models;

/**
 * A record representing user data.
 *
 * @param username the username of the user
 * @param password the password of the user
 * @param email the email address of the user
 */
public record UserData(String username, String password, String email) { }