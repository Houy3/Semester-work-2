package Server.models;

import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.Room.Room;
import Protocol.MessageValues.Room.RoomAccess;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Protocol.MessageValues.User.User;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoomDB {

    private String code;
    private Integer maxCountOfPlayers;
    private RoomAccess access;

    private final List<UserDB> users;
    private final Map<UserDB, Color> usersColor;
    private final Map<UserDB, Boolean> usersIsReady;

    private GameInitializationForm gameInitializationForm;

    public RoomDB(RoomInitializationForm form) {
        this.maxCountOfPlayers = form.getMaxCountOfPlayers();
        this.access = form.getAccess();
        this.gameInitializationForm = form.getGameInitializationForm();

        this.users = new ArrayList<>();
        this.usersColor = new HashMap<>();
        this.usersIsReady = new HashMap<>();
    }

    public Room toRoom() {
        Room room = new Room();

        room.setCode(code);
        room.setMaxCountOfPlayers(maxCountOfPlayers);
        room.setAccess(access);

        room.setUsers(users.stream().map(UserDB::toUser).collect(Collectors.toList()));

        Map<User, Color> colorMap = new HashMap<>();
        for (UserDB userDB : users) {
            colorMap.put(userDB.toUser(), usersColor.get(userDB));
        }
        room.setUsersColor(colorMap);

        Map<User, Boolean> readyMap = new HashMap<>();
        for (UserDB userDB : users) {
            readyMap.put(userDB.toUser(), usersIsReady.get(userDB));
        }
        room.setUsersIsReady(readyMap);

        return room;
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

    public List<UserDB> getUsers() {
        return users;
    }

    public Map<UserDB, Color> getUsersColor() {
        return usersColor;
    }

    public Map<UserDB, Boolean> getUsersIsReady() {
        return usersIsReady;
    }

    public GameInitializationForm getGameInitializationForm() {
        return gameInitializationForm;
    }

    public void setGameInitializationForm(GameInitializationForm gameInitializationForm) {
        this.gameInitializationForm = gameInitializationForm;
    }

    @Override
    public String toString() {
        return "RoomDB{" +
                "code='" + code + '\'' +
                ", maxCountOfPlayers=" + maxCountOfPlayers +
                ", access=" + access +
                ", users=" + users +
                ", usersColor=" + usersColor +
                ", usersIsReady=" + usersIsReady +
                ", gameInitializationForm=" + gameInitializationForm +
                '}';
    }
}
