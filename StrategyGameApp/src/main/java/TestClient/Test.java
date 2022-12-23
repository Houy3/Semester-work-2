package TestClient;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.Response.ResponseError;
import Protocol.MessageValues.Room.RoomAccess;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Protocol.MessageValues.User.UserLoginForm;
import Protocol.MessageValues.User.UserUpdateForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws BadResponseException {


        try {
            Socket socket = new Socket("localhost", 8080);

            UserLoginForm user = new UserLoginForm("email@mail.ru", "password");

            Message message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_LOGIN, user),
                    socket
            );

            if (message.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
                System.out.println("Success");
            } else if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                System.out.println(((ResponseError)message.value()).getErrorMessage());
            } else {
                System.out.println("кривой ответ");
            }


//            UserUpdateForm form = new UserUpdateForm("Houy3");
//            message = MessageManager.sendMessage(
//                    new Message(MessageManager.MessageType.USER_UPDATE, form),
//                    socket
//            );
//            if (message.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
//                System.out.println("Success");
//            } else if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
//                System.out.println(((ResponseError)message.value()).getErrorMessage());
//            } else {
//                System.out.println("кривой ответ");
//            }


            message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_PROFILE_DATA_GET, null),
                    socket
            );
            if (message.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
                System.out.println("Success");
                System.out.println(message.value().toString());
            } else if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                System.out.println(((ResponseError)message.value()).getErrorMessage());
            } else {
                System.out.println("кривой ответ");
            }

            message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.ROOM_INITIALIZE,
                            new RoomInitializationForm(
                                    4,
                                    RoomAccess.PUBLIC,
                                    Color.BLUE,
                                    new GameInitializationForm(10, 40, 35))),
                    socket
            );
            if (message.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
                System.out.println("Success");
                System.out.println(message.value().toString());
            } else if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                System.out.println(((ResponseError)message.value()).getErrorMessage());
            } else {
                System.out.println("кривой ответ");
            }


            System.out.println("end");

        } catch (IOException | MismatchedClassException e) {
            System.out.println(e.getMessage());
        }

    }
}
