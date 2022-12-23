package Protocol.MessageValues.Response;

import Protocol.MessageValues.MessageValue;

public final class ResponseError implements MessageValue {

    private String errorMessage;

    public ResponseError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    @Override
    public String toString() {
        return "ResponseError{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
