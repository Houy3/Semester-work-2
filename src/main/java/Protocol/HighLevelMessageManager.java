package Protocol;

import Protocol.MessageValues.Game.Game;
import Protocol.MessageValues.Game.GameActions.ArmyMovement;
import Protocol.MessageValues.Game.GameActions.CityCapture;
import Protocol.MessageValues.Game.GameResults;
import Protocol.MessageValues.Room.RoomConnectionForm;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Protocol.MessageValues.Room.RoomUserColor;
import Protocol.MessageValues.User.UserLoginForm;
import Protocol.MessageValues.User.UserRegistrationForm;
import Protocol.MessageValues.User.UserUpdateForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

import static Protocol.MessageManager.MessageType.*;

public class HighLevelMessageManager extends MessageManager {

    /**пустой ответ*/
    public static Message registerUser(UserRegistrationForm value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(USER_REGISTER, value), socket);
    }
    /**Возвращает User*/
    public static Message loginUser(UserLoginForm value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(USER_LOGIN, value), socket);
    }
    /**пустой ответ*/
    public static Message logoutUser(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(USER_LOGOUT, null), socket);
    }
    /**пустой ответ*/
    public static Message updateUser(UserUpdateForm value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(USER_UPDATE, value), socket);
    }
    /**Возвращает UserProfileData*/
    public static Message getUserProfileData(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(USER_PROFILE_DATA_GET, null), socket);
    }

    /**Возвращает Room*/
    public static Message initializeRoom(RoomInitializationForm value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(ROOM_INITIALIZE, value), socket);
    }
    /**Возвращает Room*/
    public static Message connectRoom(RoomConnectionForm value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(ROOM_CONNECT, value), socket);
    }
    /**пустой ответ*/
    public static Message disconnectRoom(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(ROOM_DISCONNECT, null), socket);
    }
    /**пустой ответ*/
    public static Message readyToStart(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(ROOM_I_AM_READY_TO_START, null), socket);
    }
    /**пустой ответ*/
    public static Message notReadyToStart(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(ROOM_I_AM_NOT_READY_TO_START, null), socket);
    }
    /**пустой ответ*/
    public static Message setColor(Socket socket, Color color) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(ROOM_SET_COLOR, new RoomUserColor(color)), socket);
    }

    /**Возвращает Room*/
    public static Message getRoomParameters(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(ROOM_GET, null), socket);
    }
    /**пустой ответ*/
    public static Message startGame(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GAME_START, null), socket);
    }
    /**Возвращает Game*/
    public static Message reconnectGame(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GAME_RECONNECT, null), socket);
    }
    /**пустой ответ*/
    public static Message disconnectGame(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GAME_DISCONNECT, null), socket);
    }
    /**пустой ответ. Принимает клиент*/
    public static Message gameStarted(Game value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GAME_STARTED, value), socket);
    }
    /**пустой ответ. Принимает клиент*/
    public static Message gameEnded(GameResults value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GAME_ENDED, value), socket);
    }

    /**пустой ответ. Принимает клиент*/
    public static Message moveArmy(ArmyMovement value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GAME_ACTION_ARMY_MOVEMENT, value), socket);
    }
    /**пустой ответ. Принимает клиент*/
    public static Message captureCity(CityCapture value, Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GAME_ACTION_CITY_CAPTURE, value), socket);
    }
    /**Возвращает Game*/
    public static Message getActualGameData(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GAME_DATA_GET, null), socket);
    }

    /**Возвращает List<Room>*/
    public static Message getOpenRooms(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(GET_OPEN_ROOMS, null), socket);
    }

    /**пустой ответ*/
    public static Message exit(Socket socket) throws MismatchedClassException, BadResponseException, IOException {
        return sendMessage(new Message(EXIT, null), socket);
    }
}
