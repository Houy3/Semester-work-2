package Protocol.MessageValues.Room;

import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.MessageValue;

public final class RoomParametersSetForm implements MessageValue {

    private RoomAccess access;

    private GameInitializationForm gameInitializationForm;

    public RoomParametersSetForm() {
    }

    public RoomParametersSetForm(RoomAccess access, GameInitializationForm gameInitializationForm) {
        this.access = access;
        this.gameInitializationForm = gameInitializationForm;
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
        return "RoomParametersSetForm{" +
                "access=" + access +
                ", gameInitializationForm=" + gameInitializationForm +
                '}';
    }
}
