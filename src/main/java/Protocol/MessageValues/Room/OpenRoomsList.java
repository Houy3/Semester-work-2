package Protocol.MessageValues.Room;

import Protocol.MessageValues.MessageValue;

import java.util.List;

public final class OpenRoomsList implements MessageValue {

    private List<Room> openRooms;

    public OpenRoomsList(List<Room> openRooms) {
        this.openRooms = openRooms;
    }

    public List<Room> getOpenRooms() {
        return openRooms;
    }

    public void setOpenRooms(List<Room> openRooms) {
        this.openRooms = openRooms;
    }

    @Override
    public String toString() {
        return "OpenRoomsList{" +
                "openRooms=" + openRooms +
                '}';
    }
}
