package Protocol.MessageValues.Room;

import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.User;


import java.awt.*;
import java.util.List;
import java.util.Map;

public final class Room implements MessageValue {

    private String code;
    private Integer maxCountOfPlayers;
    private RoomAccess access;

    private List<User> users;
    private Map<User, Color> usersColor;
    private Map<User, Boolean> usersIsReady;

    private GameInitializationForm gameInitializationForm;

    public Room() {}

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Map<User, Color> getUsersColor() {
        return usersColor;
    }

    public void setUsersColor(Map<User, Color> usersColor) {
        this.usersColor = usersColor;
    }

    public Map<User, Boolean> getUsersIsReady() {
        return usersIsReady;
    }

    public void setUsersIsReady(Map<User, Boolean> usersIsReady) {
        this.usersIsReady = usersIsReady;
    }

    public GameInitializationForm getGameInitializationForm() {
        return gameInitializationForm;
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
                ", usersColor=" + usersColor +
                ", isReady=" + usersIsReady +
                ", gameInitializationForm=" + gameInitializationForm +
                '}';
    }
}
