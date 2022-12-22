package Server.services.Inter;

import Protocol.MessageValues.Room.RoomConnectionForm;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Server.models.RoomDB;
import Server.models.UserDB;
import Server.models.validators.ValidatorException;

import java.util.List;

public interface RoomsService {


    RoomDB initialize(RoomInitializationForm form, UserDB user) throws ValidatorException;

    RoomDB connect(RoomConnectionForm form, UserDB user) throws ValidatorException;

    void disconnect(UserDB user);

    List<RoomDB> getOpenRooms();


}
