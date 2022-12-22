package Server.app;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.ErrorResponse;
import Protocol.MessageValues.Response.Success;
import Protocol.MessageValues.Room.Room;
import Protocol.MessageValues.User.*;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;
import Server.DB.exceptions.DBException;
import Server.DB.exceptions.NotFoundException;
import Server.DB.exceptions.NotUniqueException;
import Server.DB.exceptions.NullException;
import Server.models.UserDB;
import Server.models.validators.ValidatorException;
import Server.services.Inter.UsersService;
import Server.services.exceptions.ServiceException;
import Server.services.ServicesToolKit;
import Server.services.exceptions.UserAlreadyLoginException;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class UserConnectionThread implements Runnable {

    private final Socket socket;
    private final UsersService usersService;

    public UserConnectionThread(Socket socket, ServicesToolKit servicesToolKit) {
        this.socket = socket;
        this.usersService = servicesToolKit.getUsersService();
    }

    private UserDB userDB;

    private Room room;

    private boolean isExit = false;
    private boolean isLogout = false;


    @Override
    public void run() {
        try {
            while (!isExit) {
                loginOrRegister();
                mainLobby();
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
                        UserRegistrationForm userRegistrationForm = (UserRegistrationForm) message.value();
                        registerUserWithResponse(userRegistrationForm);
                    }
                    case USER_LOGIN -> {
                        UserLoginForm userLoginForm = (UserLoginForm) message.value();
                        loginUserWithResponse(userLoginForm);
                    }
                    case EXIT -> {
                        isExit = true;
                    }
                    default -> MessageManager.sendErrorResponse(new ErrorResponse("Firstly you need login or register"), socket.getOutputStream());
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
            MessageManager.sendErrorResponse(new ErrorResponse(e.getMessage() + " is already taken"), socket.getOutputStream());
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new ErrorResponse(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new ErrorResponse(e.getMessage()), socket.getOutputStream());
        } catch (ServiceException | DBException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new ErrorResponse("Unexpected error"), socket.getOutputStream());
        }
    }

    private void loginUserWithResponse(UserLoginForm form) throws IOException {
        try {
            userDB = new UserDB(form);
            usersService.login(userDB);
            User user = new User(userDB.getNickname());
            MessageManager.sendSuccessResponse(new Success(user), socket.getOutputStream());
            return;
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new ErrorResponse(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (NotFoundException e) {
            MessageManager.sendErrorResponse(new ErrorResponse("Email or password is incorrect"), socket.getOutputStream());
        } catch (UserAlreadyLoginException e) {
            MessageManager.sendErrorResponse(new ErrorResponse("Someone is active through this account"), socket.getOutputStream());
        } catch (DBException | ServiceException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new ErrorResponse("Unexpected error"), socket.getOutputStream());
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
                        UserUpdateForm userUpdateForm = (UserUpdateForm) message.value();
                        userUpdateWithResponse(userUpdateForm);
                    }
                    case USER_PROFILE_DATA_GET -> {
                        UserProfileData userProfileData = new UserProfileData(19);
                        MessageManager.sendSuccessResponse(new Success(userProfileData), socket.getOutputStream());
                    }
                    case EXIT -> {
                        isExit = true;
                        throw new UserDisconnectException();
                    }
                    default -> MessageManager.sendErrorResponse(new ErrorResponse("later"), socket.getOutputStream());
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

    private void userUpdateWithResponse(UserUpdateForm form) throws IOException {
        try {
            usersService.update(form, userDB);
            MessageManager.sendSuccessResponse(new Success(null), socket.getOutputStream());
        } catch (ValidatorException e) {
            MessageManager.sendErrorResponse(new ErrorResponse(e.getMessage()), socket.getOutputStream());
        } catch (NotUniqueException e) {
            MessageManager.sendErrorResponse(new ErrorResponse(e.getMessage() + " is already taken"), socket.getOutputStream());
        } catch (NullException e) {
            MessageManager.sendErrorResponse(new ErrorResponse(e.getMessage() + " can't be empty"), socket.getOutputStream());
        } catch (DBException | ServiceException | NotFoundException e) {
            errorMessageLog(e);
            MessageManager.sendErrorResponse(new ErrorResponse("Unexpected error"), socket.getOutputStream());
        }

    }



    private void gameCreation() {

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
