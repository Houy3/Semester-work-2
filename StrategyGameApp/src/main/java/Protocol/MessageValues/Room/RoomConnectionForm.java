package Protocol.MessageValues.Room;

import Protocol.MessageValues.MessageValue;

public final class RoomConnectionForm implements MessageValue {

    private String code;

    public RoomConnectionForm(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "RoomConnectionForm{" +
                "code='" + code + '\'' +
                '}';
    }
}

