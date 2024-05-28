package response;

public class CreateGameResponse extends ParentResponse{

    private Integer id;

    public CreateGameResponse(Boolean success, String message, Integer id) {
        super(success, message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
