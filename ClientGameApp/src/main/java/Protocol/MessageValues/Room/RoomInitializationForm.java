package Protocol.MessageValues.Room;

import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.MessageValue;

import java.awt.*;

public final class RoomInitializationForm implements MessageValue {

    private Integer maxCountOfPlayers;
    private RoomAccess access;

    private Color creatorColor;

    private GameInitializationForm gameInitializationForm;

    public RoomInitializationForm(Integer maxCountOfPlayers, RoomAccess access, Color creatorColor, GameInitializationForm gameInitializationForm) {
        this.maxCountOfPlayers = maxCountOfPlayers;
        this.access = access;
        this.creatorColor = creatorColor;
        this.gameInitializationForm = gameInitializationForm;
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

    public Color getCreatorColor() {
        return creatorColor;
    }

    public void setCreatorColor(Color creatorColor) {
        this.creatorColor = creatorColor;
    }

    public GameInitializationForm getGameInitializationForm() {
        return gameInitializationForm;
    }

    public void setGameInitializationForm(GameInitializationForm gameInitializationForm) {
        this.gameInitializationForm = gameInitializationForm;
    }

    @Override
    public String toString() {
        return "RoomInitializationForm{" +
                "maxCountOfPlayers=" + maxCountOfPlayers +
                ", access=" + access +
                ", gameInitializationForm=" + gameInitializationForm +
                '}';
    }
}
