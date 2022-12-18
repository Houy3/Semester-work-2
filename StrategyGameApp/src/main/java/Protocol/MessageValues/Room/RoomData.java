package Protocol.MessageValues.Room;

import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.UserData;

import java.awt.*;
import java.util.Map;

public final class RoomData implements MessageValue {

    private String code;

    private Integer maxCountOfPlayers;

    private RoomAccess access;

    private Map<UserData, Color> users;

    public RoomData() {
    }

    public RoomData(String code, Integer maxCountOfPlayers, RoomAccess access, Map<UserData, Color> users) {
        this.code = code;
        this.maxCountOfPlayers = maxCountOfPlayers;
        this.access = access;
        this.users = users;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Map<UserData, Color> getUsers() {
        return users;
    }

    public void setUsers(Map<UserData, Color> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "RoomData{" +
                "code='" + code + '\'' +
                ", maxCountOfPlayers=" + maxCountOfPlayers +
                ", access=" + access +
                ", users=" + users +
                '}';
    }
}
