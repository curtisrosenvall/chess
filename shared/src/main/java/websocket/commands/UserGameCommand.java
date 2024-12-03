package websocket.commands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {


    public UserGameCommand(String authToken, Integer gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;
    private final int gameID;
    private final String authToken;


    public String getAuthToken() {
        return authToken;
    }

    public int getGameID() {
        return this.gameID;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public void setCommandType(CommandType type) {
        this.commandType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthToken(), that.getAuthToken());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken());
    }
}
