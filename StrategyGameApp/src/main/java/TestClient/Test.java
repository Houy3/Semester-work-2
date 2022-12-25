package TestClient;

import Protocol.Message.Request;
import Protocol.Message.RequestValues.GameInitializationForm;
import Protocol.Message.RequestValues.RoomConnectionForm;
import Protocol.Message.RequestValues.RoomInitializationForm;
import Protocol.Message.RequestValues.UserLoginForm;
import Protocol.Message.ResponseValues.Room;
import Protocol.Message.models.RoomAccess;
import Protocol.MessageManager;
import Protocol.ProtocolVersionException;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class Test {

    private static String code;

    private static int PORT = 8888;

    private static Socket socket1;
    private static Socket socket2;




    public static void main(String[] args) throws IOException, ProtocolVersionException {

//        user1();
//        user2();
//
//        MessageManager.sendRequest(
//                new Request(MessageManager.MessageType.GAME_START, null),
//                socket1
//        );
//        MessageManager.sendRequest(
//                new Request(MessageManager.MessageType.GAME_START, null),
//                socket2
//        );
//
//        System.out.println("read");
//
//        Request request = MessageManager.readRequest(socket1.getInputStream());
//        System.out.println(request.type());
//        System.out.println(request.value());
//
//        System.out.println("end");

    }

//    public static void user1() throws BadResponseException {
//        try {
//            socket1 = new Socket("localhost", PORT);
//
//            UserLoginForm user = new UserLoginForm("email@mail.ru", "password");
//
//            Request request = MessageManager.sendRequest(
//                    new Request(MessageManager.MessageType.USER_LOGIN, user),
//                    socket1
//            );
//            System.out.println(request.value());
//
//            request = MessageManager.sendRequest(
//                    new Request(MessageManager.MessageType.ROOM_INITIALIZE,
//                            new RoomInitializationForm(
//                                    4,
//                                    RoomAccess.PUBLIC,
//                                    Color.RED,
//                                    new GameInitializationForm(10, 40, 35))),
//                    socket1
//            );
//            code = ((Room)((ResponseSuccess) request.value()).getResponseValue()).code();
//            System.out.println(request.value());
//
//            request = MessageManager.sendRequest(
//                    new Request(MessageManager.MessageType.ROOM_I_AM_READY_TO_START,null),
//                    socket1
//            );
//            System.out.println(request.value());
//
//        } catch (IOException | MismatchedClassException e) {
//            System.out.println(e.getMessage());
//        }
//
//    }
//
//    public static void user2() throws BadResponseException {
//        try {
//            socket2 = new Socket("localhost", PORT);
//
//            UserLoginForm user = new UserLoginForm("email2@mail.ru", "password");
//
//            Request request = MessageManager.sendRequest(
//                    new Request(MessageManager.MessageType.USER_LOGIN, user),
//                    socket2
//            );
//            System.out.println(request.value());
//
//
//            request = MessageManager.sendRequest(
//                    new Request(MessageManager.MessageType.ROOM_CONNECT,
//                            new RoomConnectionForm(code, Color.BLUE)),
//                    socket2
//            );
//            System.out.println(request.value());
//
//
//            request = MessageManager.sendRequest(
//                    new Request(MessageManager.MessageType.ROOM_I_AM_READY_TO_START,null),
//                    socket2
//            );
//            System.out.println(request.value());
//
//
//        } catch (IOException | MismatchedClassException e) {
//            System.out.println(e.getMessage());
//        }
//
//    }
}
