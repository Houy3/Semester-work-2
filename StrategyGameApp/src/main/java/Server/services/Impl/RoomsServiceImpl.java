package Server.services.Impl;

import Protocol.MessageValues.Room.RoomAccess;
import Protocol.MessageValues.Room.RoomConnectionForm;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Server.models.RoomDB;
import Server.models.UserDB;
import Server.models.validators.RoomInitValidator;
import Server.models.validators.ValidatorException;
import Server.services.Inter.RoomsService;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class RoomsServiceImpl implements RoomsService {

    private final List<RoomDB> activeRooms;
    private final Set<String> takenCodes;

    private final RoomInitValidator roomInitValidator;

    private final Lock lock;

    public RoomsServiceImpl(RoomInitValidator roomInitValidator) {
        this.activeRooms = new ArrayList<>();
        this.takenCodes = new HashSet<>();

        this.roomInitValidator = roomInitValidator;

        lock = new ReentrantLock();
    }

    @Override
    public RoomDB initialize(RoomInitializationForm form, UserDB user) throws ValidatorException {
        roomInitValidator.check(form);

        RoomDB room = createRoom(form);
        lock.lock();
        registerRoom(room);
        lock.unlock();
        addUserToRoom(room, user, form.getCreatorColor());
        return room;
    }

    @Override
    public RoomDB connect(RoomConnectionForm form, UserDB user) throws ValidatorException {
        String code = form.getCode();

        if (code == null) {
            throw new ValidatorException("Code is empty");
        }

        lock.lock();
        for (RoomDB room : activeRooms) {
            if (room.getCode().equals(code)) {
                if (room.getUsers().size() >= room.getMaxCountOfPlayers()) {
                    lock.unlock();
                    throw new ValidatorException("Room is full");
                }
                addUserToRoom(room, user, form.getColor());
                return room;
            }
        }
        lock.unlock();
        throw new ValidatorException("No room found with this code");
    }

    @Override
    public void disconnect(UserDB user) {
        RoomDB deactivateRoomDB = null;

        lock.lock();
        for (RoomDB room : activeRooms) {
            List<UserDB> roomUsers = room.getUsers();
            if (roomUsers.contains(user)) {
                disconnectUserFromRoom(room, user);
                if (roomUsers.isEmpty()) {
                    deactivateRoomDB = room;
                }
                break;
            }
        }

        if (deactivateRoomDB != null) {
            takenCodes.remove(deactivateRoomDB.getCode());
            activeRooms.remove(deactivateRoomDB);
        }
        lock.unlock();
    }

    @Override
    public List<RoomDB> getOpenRooms() {
        lock.lock();
        List<RoomDB> rooms = activeRooms.stream()
                .filter(room -> room.getAccess().equals(RoomAccess.PUBLIC)
                && room.getUsers().size() < room.getMaxCountOfPlayers())
                .collect(Collectors.toList());
        lock.unlock();
        return rooms;
    }



    private RoomDB createRoom(RoomInitializationForm form) {
        RoomDB room = new RoomDB(form);
        room.setAccess(RoomAccess.PUBLIC);

        String code = generateCode();
        room.setCode(code);
        takenCodes.add(code);

        return room;
    }

    private void registerRoom(RoomDB room) {
        activeRooms.add(room);
    }

    private void addUserToRoom(RoomDB room, UserDB user, Color color) {
        room.getUsers().add(user);
        room.getUsersIsReady().put(user, false);
        room.getUsersColor().put(user, color);
    }

    private void disconnectUserFromRoom(RoomDB room, UserDB user) {
        room.getUsers().remove(user);
        room.getUsersIsReady().remove(user);
        room.getUsersColor().remove(user);
    }


    private String generateCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0,8);
        } while (takenCodes.contains(code));
        return code;
    }


}
