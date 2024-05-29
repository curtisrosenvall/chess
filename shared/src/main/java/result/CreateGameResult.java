package result;

public class CreateGameResult extends ParentResult{

    private final Integer gameID;

    public CreateGameResult(Boolean success, String message, Integer gameID) {
        super(success, message);
        this.gameID = gameID;
    }

    public Integer getGameId() {
        return gameID;
    }
}
