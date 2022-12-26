package Protocol;

import Protocol.Message.Request;
import Protocol.Message.Response;
import Protocol.Message.ResponseValues.Game;
import Protocol.Message.RequestValues.GameActionArmyMovement;
import Protocol.Message.RequestValues.GameActionCityCapture;
import Protocol.Message.RequestValues.GameResults;
import Protocol.Message.RequestValues.RoomConnectionForm;
import Protocol.Message.RequestValues.RoomInitializationForm;
import Protocol.Message.RequestValues.RoomUserColor;
import Protocol.Message.RequestValues.UserLoginForm;
import Protocol.Message.RequestValues.UserRegistrationForm;
import Protocol.Message.RequestValues.UserUpdateForm;
import Protocol.Message.ResponseValues.ResponseError;
import Protocol.Message.ResponseValues.ResponseValue;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

import static Protocol.Message.Request.Type.*;
import static Protocol.Message.Response.Type.*;

public final class HighLevelMessageManager extends MessageManager {

    /**пустой ответ*/
    public static Response registerUser(UserRegistrationForm value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(USER_REGISTER, value), socket);
    }
    /**Возвращает User*/
    public static Response loginUser(UserLoginForm value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(USER_LOGIN, value), socket);
    }
    /**пустой ответ*/
    public static Response logoutUser(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(USER_LOGOUT, null), socket);
    }
    /**пустой ответ*/
    public static Response updateUser(UserUpdateForm value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(USER_UPDATE, value), socket);
    }


    /**Возвращает Room*/
    public static Response initializeRoom(RoomInitializationForm value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(ROOM_INITIALIZE, value), socket);
    }
    /**Возвращает Room*/
    public static Response connectToRoom(RoomConnectionForm value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(ROOM_CONNECT, value), socket);
    }
    /**пустой ответ*/
    public static Response disconnectFromRoom(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(ROOM_DISCONNECT, null), socket);
    }
    /**пустой ответ*/
    public static Response setUserReadyToStart(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(ROOM_I_AM_READY_TO_START, null), socket);
    }
    /**пустой ответ*/
    public static Response setUserNotReadyToStart(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(ROOM_I_AM_NOT_READY_TO_START, null), socket);
    }
    /**пустой ответ*/
    public static Response setPlayerNewColor(Socket socket, Color color) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(ROOM_SET_COLOR, new RoomUserColor(color)), socket);
    }
    /**Возвращает Room*/
    public static Response getRoom(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(ROOM_GET, null), socket);
    }
    /**Возвращает OpenRoomList*/
    public static Response getOpenRooms(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(GET_OPEN_ROOMS, null), socket);
    }



    /**пустой ответ*/
    public static Response startGame(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(GAME_START, null), socket);
    }
    /**пустой ответ*/
    public static Response disconnectFromGame(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(GAME_DISCONNECT, null), socket);
    }
    /**пустой ответ*/
    public static Response gameStarted(Game value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(GAME_STARTED, value), socket);
    }
    /**пустой ответ*/
    public static Response gameEnded(GameResults value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(GAME_ENDED, value), socket);
    }

    /**пустой ответ*/
    public static Response moveArmy(GameActionArmyMovement value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(GAME_ACTION_ARMY_MOVEMENT, value), socket);
    }
    /**пустой ответ*/
    public static Response captureCity(GameActionCityCapture value, Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(GAME_ACTION_CITY_CAPTURE, value), socket);
    }
    /**Возвращает Game*/
    public static Response getGame(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(GAME_DATA_GET, null), socket);
    }


    /**пустой ответ*/
    public static Response exit(Socket socket) throws IOException, ProtocolVersionException {
        return sendRequest(new Request(EXIT, null), socket);
    }


    public static void sendResponseError(String errorMessage, Socket socket) throws IOException {
        sendResponseError(new ResponseError(errorMessage), socket);
    }
    public static void sendResponseError(ResponseError responseError, Socket socket) throws IOException {
        MessageManager.sendResponse(new Response(RESPONSE_ERROR, responseError), socket);
    }

    public static void sendResponseSuccess(ResponseValue responseSuccess, Socket socket) throws IOException {
        MessageManager.sendResponse(new Response(RESPONSE_SUCCESS, responseSuccess), socket);
    }
}
