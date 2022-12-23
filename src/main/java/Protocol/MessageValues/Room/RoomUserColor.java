package Protocol.MessageValues.Room;

import Protocol.MessageValues.MessageValue;

import java.awt.*;

public final class RoomUserColor implements MessageValue {

    private Color color;

    public RoomUserColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "RoomUserColor{" +
                "color=" + color +
                '}';
    }
}
