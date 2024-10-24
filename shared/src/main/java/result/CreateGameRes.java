package result;

public class CreateGameRes extends ParentRes{

    private final Integer gameID;

    public CreateGameRes(Boolean success, String message, Integer gameID) {
        super(success, message);
        this.gameID = gameID;
    }

    public Integer getGameId() {
        return gameID;
    }
}