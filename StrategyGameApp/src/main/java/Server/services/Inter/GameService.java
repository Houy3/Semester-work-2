package Server.services.Inter;

import Protocol.MessageValues.Game.GameInitializationForm;
import Server.models.GameDB;
import Server.models.RoomDB;

public interface GameService {

    GameDB initialize(GameInitializationForm form, RoomDB roomDB);


}
