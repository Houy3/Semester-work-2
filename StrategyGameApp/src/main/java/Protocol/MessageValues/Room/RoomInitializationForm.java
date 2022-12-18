package Protocol.MessageValues.Room;

import Protocol.MessageValues.MessageValue;

public final class RoomInitializationForm implements MessageValue {

    private Integer maxCountOfPlayers;
    private RoomAccess access;

    public RoomInitializationForm(Integer maxCountOfPlayers, RoomAccess access) {
        this.maxCountOfPlayers = maxCountOfPlayers;
        this.access = access;
    }

    public Integer getMaxCountOfPlayers() {
        return maxCountOfPlayers;
    }

    public void setMaxCountOfPlayers(Integer maxCountOfPlayers) {
        this.maxCountOfPlayers = maxCountOfPlayers;
    }

    public RoomAccess getAccess() {
        return access;
    }

    public void setAccess(RoomAccess access) {
        this.access = access;
    }

}
