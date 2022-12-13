package Protocol;


import Protocol.MessageValues.Response.Error;
import Protocol.MessageValues.Response.Success;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    public final static byte VERSION = 1;

    public enum MessageTypes {
        RESPONSE_ERROR((byte)-1),
        RESPONSE_SUCCESS((byte)0),

        USER_REGISTRATION((byte)11),
        USER_AUTHENTICATION((byte)12),
        USER_DATA_UPDATE((byte)13),

        ROOM_INIT((byte)21),
        ROOM_CONNECT((byte)22),
        ROOM_DISCONNECT((byte)23),

        GAME_INIT((byte)31),
        GAME_DISCONNECT((byte)32),
        GAME_RECONNECT((byte)33),

        GAME_START((byte)34),
        GAME_END((byte)35),

        GAME_ACTION((byte)41),
        GAME_CHAT_MESSAGE((byte)42),

        GET_OPEN_ROOMS((byte)51);


        final byte value;
        MessageTypes(byte value) {
            this.value = value;
        }
    }
    private static final Map<Byte, MessageTypes> messageTypes = new HashMap<>();;
    static {
        Arrays.stream(MessageTypes.values()).
                forEach(type -> messageTypes.put(type.value, type));
    }


    //Необходимо отправить ответ после считывания сообщения
    public static Message readMessage(Socket socket) throws IOException, ProtocolException {
        InputStream in = socket.getInputStream();

        if (in.read() != VERSION) {
            throw new ProtocolException("Wrong version of protocol");
        }
        try {
            return new Message(readType(in), readValue(in));
        } catch (ClassNotFoundException e) {
            throw new ProtocolException("Wrong message value");
        }
    }

    public static void sendErrorResponse(Error error, OutputStream out) throws IOException {
        sendMessageWithoutWaitingForResponse(new Message(MessageTypes.RESPONSE_SUCCESS, error), out);
    }

    public static void sendSuccessResponse(Success success, OutputStream out) throws IOException {
        sendMessageWithoutWaitingForResponse(new Message(MessageTypes.RESPONSE_ERROR, success), out);
    }


    

    public static Message sendMessage(Message message, Socket socket) throws IOException, ProtocolException {
        sendMessageWithoutWaitingForResponse(message, socket.getOutputStream());
        
        Message response = readMessage(socket);
        if (response.type() == MessageTypes.RESPONSE_SUCCESS 
                || response.type() == MessageTypes.RESPONSE_ERROR) {
            return response;
        }
        throw new ProtocolException("Wrong response");
    }

    private static void sendMessageWithoutWaitingForResponse(Message message, OutputStream out) throws IOException {
        out.write(VERSION);
        sendType(message.type(), out);
        sendValue(message.value(), out);
    }
    private static void sendType(MessageTypes type, OutputStream out) throws IOException {
        out.write(type.value);
    }

    private static void sendValue(MessageValue value, OutputStream out) throws IOException {
        new ObjectOutputStream(out).writeObject(value);
    }


    private static MessageTypes readType(InputStream in) throws IOException {
        return messageTypes.get((byte)in.read());
    }

    private static MessageValue readValue(InputStream in) throws IOException, ClassNotFoundException {
        return (MessageValue) new ObjectInputStream(in).readObject();
    }

}
