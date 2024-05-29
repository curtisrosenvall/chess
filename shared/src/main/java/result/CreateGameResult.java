package result;

public class CreateGameResult extends ParentResult{

    private final Integer id;

    public CreateGameResult(Boolean success, String message, Integer id) {
        super(success, message);
        this.id = id;
    }

    public Integer getGameId() {
        return id;
    }
}
