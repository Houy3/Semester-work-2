package Protocol;


import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.Game.*;
import Protocol.MessageValues.Response.*;
import Protocol.MessageValues.Response.Error;
import Protocol.MessageValues.Room.*;
import Protocol.MessageValues.User.*;
import Protocol.exceptions.*;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static Protocol.MessageManager.MessageType.*;

public class MessageManager {

    public final static byte VERSION = 1;

    public enum MessageType {
        RESPONSE_ERROR((byte)-1),//содержит Error
        RESPONSE_SUCCESS((byte)0), //содержит Success с объектом

        USER_REGISTER((byte)11), //ничего не возвращает
        USER_LOGIN((byte)12), //возвращает UserData
        USER_LOGOUT((byte)13), //ничего не возвращает
        USER_DATA_GET((byte)13), //возвращает UserData
        USER_DATA_UPDATE((byte)14), //ничего не возвращает

        ROOM_INITIALIZE((byte)21), //возвращает RoomData
        ROOM_CONNECT((byte)22), //возвращает RoomData
        ROOM_DISCONNECT((byte)23), //ничего не возвращает
        ROOM_DATA_GET((byte)24), //возвращает RoomData

        GAME_INITIALIZE((byte)31), //возвращает Game
        GAME_RECONNECT((byte)32), //возвращает Game
        GAME_DISCONNECT((byte)33), //ничего не возвращает

        GAME_START((byte)34), //ничего не возвращает
        GAME_END((byte)35), //ничего не возвращает

        GAME_ACTION((byte)41), //ничего не возвращает
        GAME_CHAT_MESSAGE((byte)42), //ничего не возвращает

        GET_OPEN_ROOMS((byte)51); //возвращает List<Room>

        final byte value;
        MessageType(byte value) {
            this.value = value;
        }
    }
    //информация, при каком типе какой класс должен быть внутри Message
    //если null, то передавай null.
    private static final Map<MessageType, Class> typeToClassMap = new HashMap<>();;
    static {
        typeToClassMap.put(RESPONSE_ERROR, Error.class);
        typeToClassMap.put(RESPONSE_SUCCESS, Success.class);


        typeToClassMap.put(USER_REGISTER, UserRegistrationForm.class);
        typeToClassMap.put(USER_LOGIN, UserLoginForm.class);
        typeToClassMap.put(USER_LOGOUT, null);
        typeToClassMap.put(USER_DATA_GET, null);
        typeToClassMap.put(USER_DATA_UPDATE, UserDataUpdateForm.class);


        typeToClassMap.put(ROOM_INITIALIZE, RoomInitializationForm.class);
        typeToClassMap.put(ROOM_CONNECT, RoomConnectionForm.class);
        typeToClassMap.put(ROOM_DISCONNECT, null);
        typeToClassMap.put(ROOM_DATA_GET, null);


        typeToClassMap.put(GAME_INITIALIZE, GameInitParams.class);
        typeToClassMap.put(GAME_DISCONNECT, null);
        typeToClassMap.put(GAME_RECONNECT, null);
        typeToClassMap.put(GAME_START, Game.class);
        typeToClassMap.put(GAME_END, GameResults.class);


        typeToClassMap.put(GAME_ACTION, GameAction.class);
        typeToClassMap.put(GAME_CHAT_MESSAGE, GameChatMessage.class);


        typeToClassMap.put(GET_OPEN_ROOMS, null);


        if (values().length != typeToClassMap.size()) {
            throw new RuntimeException("Protocol is bad");
        }
    }

    private static final Map<Byte, MessageType> messageTypes = new HashMap<>();
    static {
        Arrays.stream(MessageType.values()).
                forEach(type -> messageTypes.put(type.value, type));
    }







    /**Необходимо отправить ответ после вызова этой функции*/
    public static Message readMessage(InputStream in) throws IOException, ProtocolVersionException, MismatchedClassException {
        int version = in.read();
        if (version == -1) {
            throw new IOException("Connection lost");
        }
        if (version != VERSION) {
            throw new ProtocolVersionException();
        }

        Message message = new Message(readType(in), readValue(in));
        messageValueCheck(message);
        return message;

    }

    public static void sendErrorResponse(Error error, OutputStream out) throws IOException {
        sendMessageWithoutWaitingForResponse(new Message(RESPONSE_ERROR, error), out);
    }

    public static void sendSuccessResponse(Success success, OutputStream out) throws IOException {
        sendMessageWithoutWaitingForResponse(new Message(RESPONSE_SUCCESS, success), out);
    }


    

    public static Message sendMessage(Message message, Socket socket) throws IOException, BadResponseException, MismatchedClassException {
        messageValueCheck(message);
        sendMessageWithoutWaitingForResponse(message, socket.getOutputStream());

        try {
            Message response = readMessage(socket.getInputStream());
            if (response.type() == MessageType.RESPONSE_SUCCESS
                    || response.type() == MessageType.RESPONSE_ERROR) {
                return response;
            }
            throw new BadResponseException("Not response type detected");
        } catch (ProtocolVersionException | MismatchedClassException e) {
            throw new BadResponseException(e);
        }
    }




    private static void sendMessageWithoutWaitingForResponse(Message message, OutputStream out) throws IOException {
        out.write(VERSION);
        sendType(message.type(), out);
        sendValue(message.value(), out);
        out.flush();
    }
    private static void sendType(MessageType type, OutputStream out) throws IOException {
        out.write(type.value);
    }
    private static void sendValue(MessageValue value, OutputStream out) throws IOException {
        new ObjectOutputStream(out).writeObject(value);
    }


    private static MessageType readType(InputStream in) throws IOException {
        return messageTypes.get((byte)in.read());
    }

    private static MessageValue readValue(InputStream in) throws IOException, MismatchedClassException {
        try {
            return (MessageValue) new ObjectInputStream(in).readObject();
        } catch (ClassNotFoundException e) {
            throw new MismatchedClassException("Class not found");
        }
    }

    private static void messageValueCheck(Message message) throws MismatchedClassException {
        MessageType type = message.type();
        MessageValue value = message.value();

        Class classMustBe = typeToClassMap.get(type);

        if (classMustBe == null && value == null) {
            return;
        } else if (classMustBe == null || value == null) {
            throw new MismatchedClassException();

        } else {
            if (!classMustBe.equals(value.getClass())) {
                throw new MismatchedClassException();
            }
        }
    }

}
