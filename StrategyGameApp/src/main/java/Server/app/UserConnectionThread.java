package Server.app;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.Error;
import Protocol.MessageValues.Response.Success;
import Protocol.MessageValues.User.UserData;
import Protocol.MessageValues.User.UserLoginForm;
import Protocol.MessageValues.User.UserRegistrationForm;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;
import Server.DB.exceptions.DBException;
import Server.DB.exceptions.NotFoundException;
import Server.DB.exceptions.NotUniqueException;
import Server.DB.exceptions.NullException;
import Server.DB.models.UserDB;
import Server.DB.models.validators.ValidatorException;
import Server.services.Inter.UsersService;
import Server.services.exceptions.ServiceException;
import Server.services.ServicesToolKit;
import Server.services.exceptions.UserAlreadyLoginException;
import Server.services.exceptions.UserLogoutException;

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

    @Override
    public void run() {
        try {
            while (true) {
                loginOrRegister();
                System.out.println(userDB);
                try {
                    roomConnection();
                } catch (UserLogoutException e) {
                    continue;
                }
                gameCreation();
            }
        } catch (MismatchedClassException | ProtocolVersionException e) {
            errorMessageLog(e);
        } catch (UserDisconnectException | IOException e) {}
        disconnectUser();
    }


    private void loginOrRegister() throws MismatchedClassException, ProtocolVersionException, UserDisconnectException, IOException {
        while (true) {
            Message message = MessageManager.readMessage(socket.getInputStream());
            switch (message.type()) {
                case USER_REGISTER -> {
                    registerUserWithResponse(message);
                }
                case USER_LOGIN -> {
                    loginUserWithResponse(message);
                }
                case EXIT -> {
                    throw new UserDisconnectException();
                }
                default -> MessageManager.sendErrorResponse(new Error("Firstly you need login or register"), socket.getOutputStream());
            }

            if (userDB != null) {
                break;
            }
        }
    }

    private void registerUserWithResponse(Message message) throws IOException {
        try {
            usersService.register(new UserDB((UserRegistrationForm) message.value()));
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

    private void loginUserWithResponse(Message message) throws IOException {
        userDB = new UserDB((UserLoginForm) message.value());
        try {
            usersService.login(userDB);
            UserData userData = new UserData(userDB.getNickname(), 19);
            MessageManager.sendSuccessResponse(new Success(userData), socket.getOutputStream());
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


    private void roomConnection() throws IOException, UserDisconnectException, MismatchedClassException, ProtocolVersionException, UserLogoutException {
        while (true) {
            Message message = MessageManager.readMessage(socket.getInputStream());
            switch (message.type()) {
                case USER_LOGOUT -> {
                    logoutUserWithResponse();
                }
                case EXIT -> {
                    throw new UserDisconnectException();
                }
                default -> MessageManager.sendErrorResponse(new Error("later"), socket.getOutputStream());
            }

            if (userDB == null) {
                break;
            }
        }
    }

    private void logoutUserWithResponse() throws IOException, UserLogoutException {
        logoutUser();
        MessageManager.sendSuccessResponse(new Success(null), socket.getOutputStream());
    }


    private void gameCreation() {

    }


    private void logoutUser() throws UserLogoutException {
        usersService.logout(userDB);
        userDB = null;
        throw new UserLogoutException();
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
