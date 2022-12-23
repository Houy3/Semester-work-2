package Protocol.MessageValues.Room;

import Protocol.MessageValues.MessageValue;

import java.awt.*;

public final class RoomConnectionForm implements MessageValue {

    private String code;

    private Color color;

    public RoomConnectionForm(String code, Color color) {
        this.code = code;
        this.color = color;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "RoomConnectionForm{" +
                "code='" + code + '\'' +
                ", color=" + color +
                '}';
    }
}

