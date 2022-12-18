package TestClient;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.User.UserRegistrationForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;

import java.io.IOException;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws BadResponseException {


        try {
            Socket socket = new Socket("localhost", 8080);

            UserRegistrationForm user = new UserRegistrationForm("email", "nick", "pass");

            Message message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_REGISTER, user),
                    socket
            );

            System.out.println("end");

        } catch (IOException | MismatchedClassException e) {
            System.out.println(e.getMessage());
        }

    }
}
