package result;

public class ParentResult {
    private Boolean success;
    private final String message;

    public ParentResult(Boolean success, String message) {
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }


    public void nullSuccess(){
        success = null;
    }


}
