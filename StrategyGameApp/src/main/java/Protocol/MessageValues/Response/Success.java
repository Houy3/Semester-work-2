package Protocol.MessageValues.Response;

import Protocol.MessageValues.MessageValue;

public final class Success implements MessageValue {

    private MessageValue responseValue;

    public Success(MessageValue responseValue) {
        this.responseValue = responseValue;
    }

    public MessageValue getResponseValue() {
        return responseValue;
    }

    public void setResponseValue(MessageValue responseValue) {
        this.responseValue = responseValue;
    }
}
