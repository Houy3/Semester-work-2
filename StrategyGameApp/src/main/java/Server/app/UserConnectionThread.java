package Server.app;

import Protocol.HighLevelMessageManager;
import Protocol.Message.Request;
import Protocol.Message.RequestValues.*;
import Protocol.Message.ResponseValues.OpenRoomsList;
import Protocol.Message.RequestValues.GameArmyStartMove;
import Protocol.Message.ResponseValues.ResponseError;
import Protocol.ProtocolVersionException;
import Server.DB.exceptions.DBException;
import Server.DB.exceptions.NotFoundException;
import Server.DB.exceptions.NotUniqueException;
import Server.DB.exceptions.NullException;
import Server.models.RoomDB;
import Server.models.UserDB;
import Server.models.validators.ValidatorException;
import Server.services.Inter.RoomsService;
import Server.services.Inter.UsersService;
import Server.services.ServicesToolKit;
import Server.services.exceptions.ServiceException;
import Server.services.exceptions.UserAlreadyLoginException;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.stream.Collectors;

import static Protocol.Message.Request.Type.*;


public class UserConnectionThread implements Runnable {

    private final Socket socket;
    private final UsersService usersService;

    private final RoomsService roomsService;


    public UserConnectionThread(Socket socket, ServicesToolKit servicesToolKit) {
        this.socket = socket;

        this.usersService = servicesToolKit.getUsersService();
        this.roomsService = servicesToolKit.getRoomService();
    }

    private UserDB userDB = null;
    private RoomDB roomDB = null;

    private boolean isExit = false;

    @Override
    public void run() {
        try {
            while (!isExit) {
                System.out.println("этап входа " + socket);
                loginOrRegister();

                while (isLogin() && !isExit) {
                    System.out.println("этап главного " + socket);
                    mainLobby();

                    if (!isLogin()) { break; }
                    while (isConnectedToRoom() && !isExit) {
                        System.out.println("этап лобби комнаты " + socket);
                        roomLobby();

                        if (!isConnectedToRoom() || isExit) { break; }
                        System.out.println("этап игры " + socket);
                        gameLobby();
                    }
                }
            }
        } catch (ProtocolVersionException | UserDisconnectException e) {
            errorMessageLog(e);
            exitUser();
        }
    }


    private void loginOrRegister() throws ProtocolVersionException, UserDisconnectException {
        try {
            while (!isLogin()) {
                Request request = HighLevelMessageManager.readRequest(socket);
                switch (request.type()) {
                    case USER_REGISTER -> {
                        UserRegistrationForm form = (UserRegistrationForm) request.value();
                        registerUserWithResponse(form);
                    }
                    case USER_LOGIN -> {
                        UserLoginForm form = (UserLoginForm) request.value();
                        loginUserWithResponse(form);
                    }
                    case EXIT -> {
                        exitUserWithResponse();
                    }
                    default -> HighLevelMessageManager.sendResponseError("Firstly you need login or register. ", socket);
                }
            }
        } catch (IOException e) {
            throw new UserDisconnectException("socket " + socket + " closed.");
        }
    }

    private void registerUserWithResponse(UserRegistrationForm form) throws IOException {
        try {
            usersService.register(form);
            HighLevelMessageManager.sendResponseSuccess(null, socket);
        } catch (NotUniqueException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage() + " is already taken", socket);
        } catch (NullException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage() + " can't be empty", socket);
        } catch (ValidatorException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage(), socket);
        } catch (ServiceException | DBException e) {
            errorMessageLog(e);
            HighLevelMessageManager.sendResponseError("Unexpected error", socket);
        }
    }

    private void loginUserWithResponse(UserLoginForm form) throws IOException {
        try {
            userDB = usersService.login(form);
            HighLevelMessageManager.sendResponseSuccess(userDB.toUser(), socket);
        } catch (NullException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage() + " can't be empty", socket);
        } catch (NotFoundException e) {
            HighLevelMessageManager.sendResponseError("Email or password is incorrect", socket);
        } catch (UserAlreadyLoginException e) {
            HighLevelMessageManager.sendResponseError("Someone is active through this account", socket);
        } catch (DBException | ServiceException | ValidatorException e) {
            errorMessageLog(e);
            HighLevelMessageManager.sendResponseError("Unexpected error", socket);
        }
    }

    private void exitUserWithResponse() throws IOException {
        HighLevelMessageManager.sendResponseSuccess(null, socket);
        exitUser();
    }



    private void mainLobby() throws UserDisconnectException, ProtocolVersionException {
        try {
            while (isLogin() && !isConnectedToRoom()  && !isExit) {
                Request request = HighLevelMessageManager.readRequest(socket);
                switch (request.type()) {
                    case USER_LOGOUT -> {
                        logoutUserWithResponse();
                    }
                    case USER_UPDATE -> {
                        UserUpdateForm form = (UserUpdateForm) request.value();
                        updateUserWithResponse(form);
                    }
                    case ROOM_INITIALIZE -> {
                        RoomInitializationForm form = (RoomInitializationForm) request.value();
                        initializeRoomWithResponse(form);
                    }
                    case ROOM_CONNECT -> {
                        RoomConnectionForm form = (RoomConnectionForm) request.value();
                        connectRoomWithResponse(form);
                    }
                    case GET_OPEN_ROOMS -> {
                        getOpenRoomsWithResponse();
                    }
                    case EXIT -> {
                        exitUserWithResponse();
                    }
                    default -> HighLevelMessageManager.sendResponseError("You only can: choose or create room, logout or change nickname. ", socket);
                }

            }
        } catch (IOException e) {
            throw new UserDisconnectException("socket " + socket + " closed.");
        }
    }

    private void logoutUserWithResponse() throws IOException {
        try {
            logout();
            HighLevelMessageManager.sendResponseSuccess(null, socket);
        } catch (ValidatorException e) {
            errorMessageLog(e);
            HighLevelMessageManager.sendResponseError("Unexpected error", socket);
        }
    }

    private void updateUserWithResponse(UserUpdateForm form) throws IOException {
        try {
            usersService.update(form, userDB);
            HighLevelMessageManager.sendResponseSuccess(null, socket);
        } catch (ValidatorException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage(),socket);
        } catch (NotUniqueException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage() + " is already taken", socket);
        } catch (NullException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage() + " can't be empty", socket);
        } catch (DBException | ServiceException | NotFoundException e) {
            errorMessageLog(e);
            HighLevelMessageManager.sendResponseError("Unexpected error",socket);
        }

    }

    private void initializeRoomWithResponse(RoomInitializationForm form) throws IOException {
        try {
            roomDB = roomsService.create(form);
            roomDB.addUserToRoom(userDB, form.playerColor(), socket);
            HighLevelMessageManager.sendResponseSuccess(roomDB.toRoom(), socket);
        } catch (ValidatorException e) {
            HighLevelMessageManager.sendResponseError(new ResponseError(e.getMessage()), socket);
        }
    }

    private void connectRoomWithResponse(RoomConnectionForm form) throws IOException {
        try {
            roomDB = roomsService.getRoom(form);
            roomDB.addUserToRoom(userDB, form.playerColor(), socket);
            HighLevelMessageManager.sendResponseSuccess(roomDB.toRoom(), socket);
        } catch (ValidatorException e) {
            roomDB = null;
            HighLevelMessageManager.sendResponseError(e.getMessage(), socket);
        }
    }

    private void getOpenRoomsWithResponse() throws IOException {
        OpenRoomsList openRoomsList =
                new OpenRoomsList(
                        roomsService.getOpenRooms() //получаем открытые комнаты
                                .stream().map(RoomDB::toRoom).collect(Collectors.toList())); // переводим в тип для протокола
        HighLevelMessageManager.sendResponseSuccess(openRoomsList, socket);
    }


    private void roomLobby() throws UserDisconnectException, ProtocolVersionException {
        try {
            while (isConnectedToRoom() && !roomDB.isReady(userDB) && !isExit) {
                Request request = HighLevelMessageManager.readRequest(socket);
                switch (request.type()) {
                    case ROOM_DISCONNECT -> {
                        disconnectUserFromRoomWithResponse();
                    }
                    case ROOM_I_AM_READY_TO_START -> {
                        setReadyToStartWithResponse();
                    }
                    case ROOM_SET_COLOR -> {
                        RoomUserColor roomUserColor = (RoomUserColor) request.value();
                        setUserColorInRoom(roomUserColor);
                    }
                    case ROOM_GET -> {
                        getRoomParametersWithResponse();
                    }
                    case EXIT -> {
                        exitUserWithResponse();
                    }
                    default -> HighLevelMessageManager.sendResponseError("You only can: disconnect from room, become ready or set your color. ", socket);
                }

            }
        } catch (IOException e) {
            throw new UserDisconnectException("socket " + socket + " closed.");
        }
    }

    private void disconnectUserFromRoomWithResponse() throws IOException {
        disconnectFromRoom();
        HighLevelMessageManager.sendResponseSuccess(null, socket);
    }
    private void setReadyToStartWithResponse() throws IOException {
        roomDB.setUserIsReady(userDB);
        HighLevelMessageManager.sendResponseSuccess(null, socket);
    }
    private void setUserColorInRoom(RoomUserColor roomUserColor) throws IOException {
        try {
            roomDB.setUserColor(userDB, roomUserColor.color());
            HighLevelMessageManager.sendResponseSuccess(null, socket);
        } catch (ValidatorException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage(), socket);
        }
    }
    private void getRoomParametersWithResponse() throws IOException {
        HighLevelMessageManager.sendResponseSuccess(roomDB.toRoom(), socket);
    }

    private void gameLobby() throws UserDisconnectException, ProtocolVersionException {
        try {
            while (roomDB.isReady(userDB)) {
                Request request = HighLevelMessageManager.readRequest(socket);

                if (!roomDB.isGameInProcess()) {
                    switch (request.type()) {
                        case ROOM_GET -> {
                            getRoomParametersWithResponse();
                        }
                        case ROOM_I_AM_NOT_READY_TO_START -> {
                            setNotReadyToStartWithResponse();
                        }
                        case GAME_START -> {
                            startGameWithResponse();
                        }
                        case EXIT -> {
                            exitUserWithResponse();
                        }
                        default -> HighLevelMessageManager.sendResponseError("You are ready to game. You can only: become not ready or start game. ", socket);

                    }
                } else {
                    switch (request.type()) {
                        case GAME_DISCONNECT -> {
                            disconnectUserFromGameWithResponse();
                        }
                        case GAME_ACTION_ARMY_START_MOVE ->  {
                            GameArmyStartMove gameArmyStartMove = (GameArmyStartMove) request.value();
                            moveArmyStartWithResponse(gameArmyStartMove);
                        }
                        case GAME_DATA_GET -> {
                            getGameWithResponse();
                        }
                        case EXIT -> {
                            exitUserWithResponse();
                        }
                        default -> HighLevelMessageManager.sendResponseError("You are in game. You can only: disconnect from game (and room) or move army. ",socket);

                    }
                }
            }

        } catch (IOException e) {
            throw new UserDisconnectException("socket " + socket + " closed.");
        }
    }


    private void setNotReadyToStartWithResponse() throws IOException {
        roomDB.setUserIsNotReady(userDB);
        HighLevelMessageManager.sendResponseSuccess(null, socket);
    }
    private void startGameWithResponse() throws IOException {
        try {
            roomDB.startGame();
            HighLevelMessageManager.sendResponseSuccess(null, socket);
            sendMessageToAllUsersInRoom(new Request(Request.Type.GAME_STARTED, roomDB.getGameDB().toGame()));
        } catch (ValidatorException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage(), socket);
        }

    }

    private void disconnectUserFromGameWithResponse() throws IOException {
        HighLevelMessageManager.sendResponseSuccess(null, socket);
        disconnectFromRoom();
    }

    private void moveArmyStartWithResponse(GameArmyStartMove gameArmyStartMove) throws IOException {
        try {
            roomDB.getGameDB().moveArmy(gameArmyStartMove, userDB, this);
            HighLevelMessageManager.sendResponseSuccess(null, socket);
            sendMessageToAllUsersInRoom(new Request(GAME_ACTION_ARMY_START_MOVE, gameArmyStartMove));
        } catch (ValidatorException e) {
            HighLevelMessageManager.sendResponseError(e.getMessage(), socket);
        }
    }
    public void moveArmyEnd(GameArmyEndMove gameArmyEndMove) {
        if (gameArmyEndMove == null) {
            throw new RuntimeException("game army end move");
        }
        sendMessageToAllUsersInRoom(new Request(GAME_ACTION_ARMY_END_MOVE, gameArmyEndMove));

        if (roomDB.getGameDB().isEnd()) {
            sendMessageToAllUsersInRoom(
                    new Request(GAME_ENDED,
                    new GameResults(roomDB.getGameDB().getWinner().toUser()))
            );
        }
    }

    private void getGameWithResponse() throws IOException {
        HighLevelMessageManager.sendResponseSuccess(roomDB.getGameDB().toGame(), socket);
    }



    private void sendMessageToAllUsersInRoom(Request request) {
        roomDB.getRoomLock().lock();
        for (Socket socket : roomDB.getSockets()) {
            try {
                HighLevelMessageManager.sendRequestWithOutResponse(request, socket);
            } catch (IOException | ProtocolVersionException ignored) {}
        }
        roomDB.getRoomLock().unlock();
    }


    private boolean isLogin() {
        return userDB != null;
    }

    private boolean isConnectedToRoom() {
        return roomDB != null;
    }


    private void logout() throws ValidatorException {
        usersService.logout(userDB);
        userDB = null;
    }

    private void disconnectFromRoom() {
        if (roomDB.isGameInProcess()) {
            roomDB.getGameDB().disconnectUser(userDB, this);
        }
        roomDB.removeUserFromRoom(userDB);
        roomsService.remove(roomDB);
        roomDB = null;
    }


    private void exitUser() {
        if (isConnectedToRoom()) {
            disconnectFromRoom();
        }
        try {
            logout();
        } catch (ValidatorException ignored) {}
        closeConnection();
        isExit = true;
    }

    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException ignored) {}
    }

    private void errorMessageLog(Exception e) {
        System.out.println(new Date() + " " + e.getMessage());
    }
}
