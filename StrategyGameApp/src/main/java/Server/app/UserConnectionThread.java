package Server.app;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.Error;
import Protocol.MessageValues.Response.Success;
import Protocol.MessageValues.Room.OpenRoomsList;
import Protocol.MessageValues.Room.Room;
import Protocol.MessageValues.Room.RoomConnectionForm;
import Protocol.MessageValues.Room.RoomInitializationForm;
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
import java.util.List;
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
    private boolean isLogout = false;


    @Override
    public void run() {
        try {
            while (!isExit) {
                loginOrRegister();
                mainLobby();
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (MismatchedClassException | ProtocolVersionException e) {
            errorMessageLog(e);
        } catch (UserDisconnectException e) {}
        disconnectUser();
    }


    private void loginOrRegister() throws MismatchedClassException, ProtocolVersionException, UserDisconnectException {
        try {
            while (userDB == null) {
                Message message = MessageManager.readMessage(socket.getInputStream());
                switch (message.type()) {
                    case USER_REGISTER -> {
                        UserRegistrationForm form = (UserRegistrationForm) message.value();
                        registerUserWithResponse(form);
                    }
                    case USER_LOGIN -> {
                        UserLoginForm form = (UserLoginForm) message.value();
                        loginUserWithResponse(form);
                    }
                    case EXIT -> {
                        isExit = true;
                    }
                    default -> MessageManager.sendErrorResponse(new Error("Firstly you need login or register"), socket.getOutputStream());
                }
            }
        } catch (IOException e) {
            throw new UserDisconnectException();
        }
    }

    private void registerUserWithResponse(UserRegistrationForm form) throws IOException {
        try {
            usersService.register(new UserDB(form));
            MessageManager.sendSuccessResponse(new Success(null), socket.getOutputStream());
        } catch (NotUniqueException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage() + " is already taken"), socket.getOutputStream());
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage()), socket.getOutputStream());
        } catch (ServiceException | DBException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new Error("Unexpected error"), socket.getOutputStream());
        }
    }

    private void loginUserWithResponse(UserLoginForm form) throws IOException {
        try {
            userDB = new UserDB(form);
            usersService.login(userDB);
            User user = new User(this.userDB.getNickname());
            MessageManager.sendSuccessResponse(new Success(user), socket.getOutputStream());
            return;
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (NotFoundException e) {
            MessageManager.sendErrorResponse(new Error("Email or password is incorrect"), socket.getOutputStream());
        } catch (UserAlreadyLoginException e) {
            MessageManager.sendErrorResponse(new Error("Someone is active through this account"), socket.getOutputStream());
        } catch (DBException | ServiceException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new Error("Unexpected error"), socket.getOutputStream());
        }
        userDB = null;
    }


    private void mainLobby() throws UserDisconnectException, MismatchedClassException, ProtocolVersionException {
        try {

            while (true) {
                Message message = MessageManager.readMessage(socket.getInputStream());
                switch (message.type()) {
                    case USER_LOGOUT -> {
                        isLogout = true;
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
                        isExit = true;
                        throw new UserDisconnectException();
                    }
                    default -> MessageManager.sendErrorResponse(new Error("later"), socket.getOutputStream());
                }

                if (isLogout || isExit) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new UserDisconnectException();
        }
    }

    private void logoutUserWithResponse() throws IOException {
        logoutUser();
        MessageManager.sendSuccessResponse(new Success(null), socket.getOutputStream());
    }

    private void updateUserWithResponse(UserUpdateForm form) throws IOException {
        try {
            usersService.update(form, userDB);
            MessageManager.sendSuccessResponse(new Success(null), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage()), socket.getOutputStream());
        } catch (NotUniqueException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage() + " is already taken"), socket.getOutputStream());
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (DBException | ServiceException | NotFoundException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new Error("Unexpected error"), socket.getOutputStream());
        }

    }

    private void getUserProfileDataWithResponse() throws IOException {
        UserProfileData userProfileData = usersService.getProfileData(userDB);
        MessageManager.sendSuccessResponse(new Success(userProfileData), socket.getOutputStream());
    }

    private void initializeRoomWithResponse(RoomInitializationForm form) throws IOException {
        try {
            roomDB = roomsService.initialize(form, userDB);
            MessageManager.sendSuccessResponse(new Success(roomDB.toRoom()), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage()), socket.getOutputStream());
        }
    }

    private void connectRoomWithResponse(RoomConnectionForm form) throws IOException {
        try {
            roomDB = roomsService.connect(form, userDB);
            MessageManager.sendSuccessResponse(new Success(roomDB.toRoom()), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new Error(e.getMessage()), socket.getOutputStream());
        }
    }

    private void getOpenRoomsWithResponse() throws IOException {
        List<RoomDB> openRoomsDB = roomsService.getOpenRooms();
        List<Room> openRooms =  openRoomsDB.stream().map(RoomDB::toRoom).collect(Collectors.toList());
        OpenRoomsList openRoomsList = new OpenRoomsList(openRooms);
        MessageManager.sendSuccessResponse(new Success(openRoomsList), socket.getOutputStream());
    }



    private void logoutUser() {
        usersService.logout(userDB);
        userDB = null;
    }

    private void disconnectUser() {
        try {
            logoutUser();
            socket.close();
        } catch (Exception ignored) {}
    }


    private void errorMessageLog(Exception e) {
        System.out.println(new Date() + " " + e.getMessage());
    }
}
