package Protocol.MessageValues.Room;

import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.MessageValue;

public final class RoomInitializationForm implements MessageValue {

    private Integer maxCountOfPlayers;
    private RoomAccess access;

    private GameInitializationForm gameInitializationForm;

    public RoomInitializationForm(Integer maxCountOfPlayers, RoomAccess access, GameInitializationForm gameInitializationForm) {
        this.maxCountOfPlayers = maxCountOfPlayers;
        this.access = access;
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
