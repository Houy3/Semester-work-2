package TestClient;

import Protocol.HighLevelMessageManager;
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
            HighLevelMessageManager messageManager = new HighLevelMessageManager();

            Socket socket =  new Socket("localhost", 8888);

            System.out.println(Message.class);
            
            if (message.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
                System.out.println("Success");
            } else if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                System.out.println(((ResponseError)message.value()).getErrorMessage());
            } else {
                System.out.println("кривой ответ");
            }

            UserRegistrationForm userRegistrationForm  = new UserRegistrationForm(
                    "naursdsduz@gmail.com",
                    "nauruz0304",
                    "asdasdasd"
            );


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

            System.out.println(register.type());
         //   System.out.println(MessageManager.readMessage(socket.getInputStream()));


        } catch (IOException | MismatchedClassException e) {
            System.out.println(e.getMessage());
        }

    }
}
