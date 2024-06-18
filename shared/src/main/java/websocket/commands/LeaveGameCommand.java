package websocket.commands;

public class LeaveGameCommand extends UserGameCommand{

    private Integer gameID;

    public LeaveGameCommand(String authToken, Integer gameID) {
        super(authToken, gameID);
        this.commandType = CommandType.LEAVE;
    }

}
