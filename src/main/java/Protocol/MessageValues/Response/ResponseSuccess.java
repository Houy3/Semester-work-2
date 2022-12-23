package Protocol.MessageValues.Response;

import Protocol.MessageValues.MessageValue;

public final class ResponseSuccess implements MessageValue {

    private MessageValue responseValue;

    public ResponseSuccess(MessageValue responseValue) {
        this.responseValue = responseValue;
    }

    public MessageValue getResponseValue() {
        return responseValue;
    }

    public void setResponseValue(MessageValue responseValue) {
        this.responseValue = responseValue;
    }

    @Override
    public String toString() {
        return "ResponseSuccess{" +
                "responseValue=" + responseValue +
                '}';
    }
}
