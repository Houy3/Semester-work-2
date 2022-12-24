package TestClient;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.Response.ResponseError;
import Protocol.MessageValues.Response.ResponseSuccess;
import Protocol.MessageValues.Room.Room;
import Protocol.MessageValues.Room.RoomAccess;
import Protocol.MessageValues.Room.RoomConnectionForm;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Protocol.MessageValues.User.UserLoginForm;
import Protocol.MessageValues.User.UserUpdateForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class Test {

    private static String code;

    private static int PORT = 8888;

    private static Socket socket1;
    private static Socket socket2;




    public static void main(String[] args) throws BadResponseException, MismatchedClassException, IOException, ProtocolVersionException {

        user1();
        user2();

        MessageManager.sendMessage(
                new Message(MessageManager.MessageType.GAME_START, null),
                socket1
        );

        System.out.println("read");

        Message message = MessageManager.readMessage(socket1.getInputStream());
        System.out.println(message.type());
        System.out.println(message.value());

        System.out.println("end");

    }

    public static void user1() throws BadResponseException {
        try {
            socket1 = new Socket("localhost", PORT);

            UserLoginForm user = new UserLoginForm("email@mail.ru", "password");

            Message message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_LOGIN, user),
                    socket1
            );
            System.out.println(message.value());

            message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.ROOM_INITIALIZE,
                            new RoomInitializationForm(
                                    4,
                                    RoomAccess.PUBLIC,
                                    Color.RED,
                                    new GameInitializationForm(10, 40, 35))),
                    socket1
            );
            code = ((Room)((ResponseSuccess)message.value()).getResponseValue()).getCode();
            System.out.println(message.value());

            message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.ROOM_I_AM_READY_TO_START,null),
                    socket1
            );
            System.out.println(message.value());

        } catch (IOException | MismatchedClassException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void user2() throws BadResponseException {
        try {
            socket2 = new Socket("localhost", PORT);

            UserLoginForm user = new UserLoginForm("email2@mail.ru", "password");

            Message message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_LOGIN, user),
                    socket2
            );
            System.out.println(message.value());


            message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.ROOM_CONNECT,
                            new RoomConnectionForm(code, Color.BLUE)),
                    socket2
            );
            System.out.println(message.value());


            message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.ROOM_I_AM_READY_TO_START,null),
                    socket2
            );
            System.out.println(message.value());


        } catch (IOException | MismatchedClassException e) {
            System.out.println(e.getMessage());
        }

    }
}
