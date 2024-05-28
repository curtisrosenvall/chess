package response;

public class ParentResponse {
    private String message;
    private Boolean success;

    public ParentResponse(Boolean success, String message) {
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void nullParentVariables(){
        message = null;
        success = null;
    }

    public void nullSuccess(){
        success = null;
    }


}
