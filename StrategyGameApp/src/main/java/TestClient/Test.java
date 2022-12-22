package TestClient;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.Error;
import Protocol.MessageValues.User.UserLoginForm;
import Protocol.MessageValues.User.UserRegistrationForm;
import Protocol.MessageValues.User.UserUpdateForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

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
                System.out.println(((Error)message.value()).getErrorMessage());
            } else {
                System.out.println("кривой ответ");
            }


            UserUpdateForm form = new UserUpdateForm("Houy3");
            message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_UPDATE, form),
                    socket
            );
            if (message.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
                System.out.println("Success");
            } else if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                System.out.println(((Error)message.value()).getErrorMessage());
            } else {
                System.out.println("кривой ответ");
            }


            message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_PROFILE_DATA_GET, null),
                    socket
            );
            if (message.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
                System.out.println("Success");
            } else if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                System.out.println(((Error)message.value()).getErrorMessage());
            } else {
                System.out.println("кривой ответ");
            }


            System.out.println("end");

        } catch (IOException | MismatchedClassException e) {
            System.out.println(e.getMessage());
        }

    }
}
