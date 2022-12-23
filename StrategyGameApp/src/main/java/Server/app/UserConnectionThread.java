package Server.app;

import Protocol.Message;
import Protocol.MessageManager;

import Protocol.MessageValues.Response.ResponseError;
import Protocol.MessageValues.Response.ResponseSuccess;
import Protocol.MessageValues.Room.*;
import Protocol.MessageValues.User.*;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;
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

public class UserConnectionThread implements Runnable {

    private final Socket socket;
    private final UsersService usersService;

    private final RoomsService roomsService;


    public UserConnectionThread(Socket socket, ServicesToolKit servicesToolKit) {
        this.socket = socket;
        this.usersService = servicesToolKit.getUsersService();
        this.roomsService = servicesToolKit.getRoomService();
    }

    private UserDB userDB;

    private RoomDB roomDB;

    private boolean isExit = false;
    private boolean isLogout = true;

    private boolean isRoomConnected = false;


    @Override
    public void run() {
        try {
            while (!isExit) {
                System.out.println("этап входа");
                loginOrRegister();
                if (isExit) {continue;}
                System.out.println("этап лобби");
                mainLobby();
                if (isLogout) {continue;}
                while (!isExit) {
                    System.out.println("этап комнаты");
                    roomLobby();
                    if (isExit) {break;}
                    System.out.println("этап игры");
                    gameLobby();
                }
            }
        } catch (MismatchedClassException | ProtocolVersionException | UserDisconnectException e) {
            errorMessageLog(e);
            disconnectUser();
        }
    }


    private void loginOrRegister() throws MismatchedClassException, ProtocolVersionException, UserDisconnectException {
        try {
            while (isLogout) {
                Message message = MessageManager.readMessage(socket.getInputStream());
                switch (message.type()) {
                    case USER_REGISTER -> {
                        UserRegistrationForm form = (UserRegistrationForm) message.value();
                        registerUserWithResponse(form);
                    }
                    case USER_LOGIN -> {
                        UserLoginForm form = (UserLoginForm) message.value();
                        loginUserWithResponse(form);
                        isLogout = false;
                    }
                    case EXIT -> {
                        exitUser();
                    }
                    default -> MessageManager.sendErrorResponse(new ResponseError("Firstly you need login or register"), socket.getOutputStream());
                }
            }
        } catch (IOException e) {
            throw new UserDisconnectException("socket closed");
        }
    }

    private void registerUserWithResponse(UserRegistrationForm form) throws IOException {
        try {
            usersService.register(new UserDB(form));
            MessageManager.sendSuccessResponse(new ResponseSuccess(null), socket.getOutputStream());
        } catch (NotUniqueException e) {

            MessageManager.sendErrorResponse(new ResponseError(e.getMessage() + " is already taken"), socket.getOutputStream());
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new ResponseError(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new ResponseError(e.getMessage()), socket.getOutputStream());
        } catch (ServiceException | DBException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new ResponseError("Unexpected error"), socket.getOutputStream());
        }
    }

    private void loginUserWithResponse(UserLoginForm form) throws IOException {
        try {
            loginUser(new UserDB(form));
            MessageManager.sendSuccessResponse(new ResponseSuccess(userDB.toUser()), socket.getOutputStream());
            return;
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new ResponseError(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (NotFoundException e) {
            MessageManager.sendErrorResponse(new ResponseError("Email or password is incorrect"), socket.getOutputStream());
        } catch (UserAlreadyLoginException e) {
            MessageManager.sendErrorResponse(new ResponseError("Someone is active through this account"), socket.getOutputStream());
        } catch (DBException | ServiceException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new ResponseError("Unexpected error"), socket.getOutputStream());
        }
        userDB = null;
    }


    private void mainLobby() throws UserDisconnectException, MismatchedClassException, ProtocolVersionException {
        try {
            while (!isLogout && !isExit && !isRoomConnected) {
                Message message = MessageManager.readMessage(socket.getInputStream());
                switch (message.type()) {
                    case USER_LOGOUT -> {
                        logoutUserWithResponse();
                    }
                    case USER_UPDATE -> {
                        UserUpdateForm form = (UserUpdateForm) message.value();
                        updateUserWithResponse(form);
                    }
                    case USER_PROFILE_DATA_GET -> {
                        getUserProfileDataWithResponse();
                    }
                    case ROOM_INITIALIZE -> {
                        RoomInitializationForm form = (RoomInitializationForm) message.value();
                        initializeRoomWithResponse(form);
                    }
                    case ROOM_CONNECT -> {
                        RoomConnectionForm form = (RoomConnectionForm) message.value();
                        connectRoomWithResponse(form);
                    }
                    case GET_OPEN_ROOMS -> {
                        getOpenRoomsWithResponse();
                    }
                    case EXIT -> {
                        exitUser();
                    }
                    default -> MessageManager.sendErrorResponse(new ResponseError("later"), socket.getOutputStream());
                }

            }
        } catch (IOException e) {
            throw new UserDisconnectException("socket closed");
        }
    }

    private void logoutUserWithResponse() throws IOException {
        logoutUser();
        MessageManager.sendSuccessResponse(new ResponseSuccess(null), socket.getOutputStream());
    }

    private void updateUserWithResponse(UserUpdateForm form) throws IOException {
        try {
            usersService.update(form, userDB);
            MessageManager.sendSuccessResponse(new ResponseSuccess(null), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new ResponseError(e.getMessage()), socket.getOutputStream());
        } catch (NotUniqueException e) {
            MessageManager.sendErrorResponse(new ResponseError(e.getMessage() + " is already taken"), socket.getOutputStream());
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new ResponseError(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (DBException | ServiceException | NotFoundException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new ResponseError("Unexpected error"), socket.getOutputStream());
        }

    }

    private void getUserProfileDataWithResponse() throws IOException {
        UserProfileData userProfileData = usersService.getProfileData(userDB);
        MessageManager.sendSuccessResponse(new ResponseSuccess(userProfileData), socket.getOutputStream());
    }

    private void initializeRoomWithResponse(RoomInitializationForm form) throws IOException {
        try {
            connectToRoom(roomsService.initialize(form, userDB));
            MessageManager.sendSuccessResponse(new ResponseSuccess(roomDB.toRoom()), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new ResponseError(e.getMessage()), socket.getOutputStream());
        }
    }

    private void connectRoomWithResponse(RoomConnectionForm form) throws IOException {
        try {
            connectToRoom(roomsService.connect(form, userDB));
            MessageManager.sendSuccessResponse(new ResponseSuccess(roomDB.toRoom()), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new ResponseError(e.getMessage()), socket.getOutputStream());
        }
    }

    private void getOpenRoomsWithResponse() throws IOException {
        OpenRoomsList openRoomsList =
                new OpenRoomsList(
                        roomsService.getOpenRooms() //получаем открытые комнаты
                                .stream().map(RoomDB::toRoom).collect(Collectors.toList())); // переводим в тип для протокола
        MessageManager.sendSuccessResponse(new ResponseSuccess(openRoomsList), socket.getOutputStream());
    }



    private void roomLobby() throws UserDisconnectException, MismatchedClassException, ProtocolVersionException {
        try {
            while (!isExit && isRoomConnected) {
                Message message = MessageManager.readMessage(socket.getInputStream());
                switch (message.type()) {
                    case ROOM_DISCONNECT -> {
                        disconnectUserFromRoomWithResponse();
                    }
                    case ROOM_I_AM_READY_TO_START -> {
                        setReadyToStartWithResponse();
                    }
                    case ROOM_I_AM_NOT_READY_TO_START -> {
                        setNotReadyToStartWithResponse();
                    }
                    case ROOM_GET -> {
                        getRoomParametersWithResponse();
                    }
                    case GAME_START -> {
                        startGameWithResponse();
                    }
                    case EXIT -> {
                        exitUser();
                    }
                    default -> MessageManager.sendErrorResponse(new ResponseError("later"), socket.getOutputStream());
                }

            }
        } catch (IOException e) {
            throw new UserDisconnectException("socket closed");
        }
    }

    private void disconnectUserFromRoomWithResponse() throws IOException {
        disconnectFromRoom();
        MessageManager.sendSuccessResponse(new ResponseSuccess(null), socket.getOutputStream());
    }

    private void setReadyToStartWithResponse() throws IOException {
        roomDB.getUsersIsReady().replace(userDB, true);
        MessageManager.sendSuccessResponse(new ResponseSuccess(null), socket.getOutputStream());
    }

    private void setNotReadyToStartWithResponse() throws IOException {
        roomDB.getUsersIsReady().replace(userDB, false);
        MessageManager.sendSuccessResponse(new ResponseSuccess(null), socket.getOutputStream());
    }

    private void getRoomParametersWithResponse() throws IOException {
        MessageManager.sendSuccessResponse(new ResponseSuccess(roomDB.toRoom()), socket.getOutputStream());
    }


    private void startGameWithResponse() throws IOException {
        MessageManager.sendSuccessResponse(new ResponseSuccess(null), socket.getOutputStream());
        //TODO
    }

    private void gameLobby() {

    }


    private void loginUser(UserDB user) throws DBException, UserAlreadyLoginException, ServiceException, NotFoundException, NullException {
        usersService.login(user);
        userDB = user;
        isLogout = false;
    }

    private void logoutUser() {
        usersService.logout(userDB);
        userDB = null;
        isLogout = true;
    }

    private void connectToRoom(RoomDB room) {
        roomDB = room;
        isRoomConnected = true;
    }

    private void disconnectFromRoom() {
        roomsService.disconnect(userDB);
        roomDB = null;
        isRoomConnected = false;
    }

    private void exitUser() {
        disconnectFromRoom();
        logoutUser();
        closeConnection();
        isExit = true;
    }
    private void disconnectUser() {
        logoutUser();
        closeConnection();
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
