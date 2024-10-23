package result;

public class CreateGameRes extends ParentRes{

    private final Integer id;

    public CreateGameRes(Boolean success, String message, Integer id) {
        super(success, message);
        this.id = id;
    }

    public Integer getGameId() {
        return id;
    }
}