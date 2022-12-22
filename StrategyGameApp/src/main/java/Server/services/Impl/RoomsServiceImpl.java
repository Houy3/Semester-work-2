package Server.services.Impl;

import Protocol.MessageValues.Room.RoomAccess;
import Protocol.MessageValues.Room.RoomConnectionForm;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Server.models.RoomDB;
import Server.models.UserDB;
import Server.models.validators.RoomInitValidator;
import Server.models.validators.ValidatorException;
import Server.services.Inter.RoomsService;

import java.util.*;
import java.util.stream.Collectors;

public class RoomsServiceImpl implements RoomsService {

    private final List<RoomDB> activeRooms;
    private final Set<String> takenCodes;

    private final RoomInitValidator roomInitValidator;


    public RoomsServiceImpl(RoomInitValidator roomInitValidator) {
        this.roomInitValidator = roomInitValidator;
        activeRooms = new ArrayList<>();
        takenCodes = new HashSet<>();
    }

    @Override
    public RoomDB initialize(RoomInitializationForm form, UserDB user) throws ValidatorException {
        roomInitValidator.check(form);

        RoomDB room = initialize(form);
        List<UserDB> roomUsers = room.getUsers();
        roomUsers.add(user);
        return room;
    }

    @Override
    public RoomDB connect(RoomConnectionForm form, UserDB user) throws ValidatorException {
        String code = form.getCode();

        if (code == null) {
            throw new ValidatorException("Code is empty");
        }

        for (RoomDB room : activeRooms) {
            if (room.getCode().equals(code)) {
                if (room.getUsers().size() >= room.getMaxCountOfPlayers()) {
                    throw new ValidatorException("Room is full");
                }
                room.getUsers().add(user);
                return room;
            }
        }
        throw new ValidatorException("No room found with this code");
    }

    @Override
    public void disconnect(UserDB user) {
        RoomDB deactivateRoomDB = null;

        for (RoomDB room : activeRooms) {
            List<UserDB> roomUsers = room.getUsers();
            if (roomUsers.contains(user)) {
                roomUsers.remove(user);
                if (roomUsers.isEmpty()) {
                    deactivateRoomDB = room;
                }
                break;
            }
        }

        if (deactivateRoomDB != null) {
            activeRooms.remove(deactivateRoomDB);
        }
    }

    @Override
    public List<RoomDB> getOpenRooms() {
        return activeRooms.stream().filter(room -> room.getAccess().equals(RoomAccess.PUBLIC)).collect(Collectors.toList());
    }



    private RoomDB initialize(RoomInitializationForm form) {
        RoomDB room = new RoomDB(form);

        String code = generateCode();
        room.setCode(code);
        takenCodes.add(code);

        return room;
    }

    private String generateCode() {
        String code = "";
        while (takenCodes.contains(code)) {
            code = UUID.randomUUID().toString().substring(0,8);
        }
        return code;
    }
}
