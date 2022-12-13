package Protocol.MessageValues.Response;

import Protocol.MessageValue;

public final class Error extends MessageValue {

    private String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
