package Protocol.MessageValues.Room;

import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.User;

import java.awt.*;
import java.util.Map;

public final class Room implements MessageValue {

    private String code;
    private Integer maxCountOfPlayers;
    private RoomAccess access;

    private Map<User, Color> users;
    private Map<User, Boolean> isReady;

    private GameInitializationForm gameInitializationForm;


    public Room() {
    }

    public Room(String code, Integer maxCountOfPlayers, RoomAccess access, Map<User, Color> users, Map<User, Boolean> isReady, GameInitializationForm gameInitializationForm) {
        this.code = code;
        this.maxCountOfPlayers = maxCountOfPlayers;
        this.access = access;
        this.users = users;
        this.isReady = isReady;
        this.gameInitializationForm = gameInitializationForm;
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

    public Map<User, Color> getUsers() {
        return users;
    }

    public void setUsers(Map<User, Color> users) {
        this.users = users;
    }

    public GameInitializationForm getGameInitializationForm() {
        return gameInitializationForm;
    }

    public Map<User, Boolean> getIsReady() {
        return isReady;
    }

    public void setIsReady(Map<User, Boolean> isReady) {
        this.isReady = isReady;
    }

    public void setGameInitializationForm(GameInitializationForm gameInitializationForm) {
        this.gameInitializationForm = gameInitializationForm;
    }

    @Override
    public String toString() {
        return "Room{" +
                "code='" + code + '\'' +
                ", maxCountOfPlayers=" + maxCountOfPlayers +
                ", access=" + access +
                ", users=" + users +
                ", isReady=" + isReady +
                ", gameInitializationForm=" + gameInitializationForm +
                '}';
    }
}
