package Client;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Game.Game;
import Protocol.MessageValues.User.User;
import Protocol.exceptions.BadResponseException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws BadResponseException {


        try {
            Socket socket = new Socket("localhost", 8080);

            User user = new User("email", "nick", "pass");

            Message message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_AUTHENTICATION, new Game()),
                    socket
            );

            System.out.println("end");



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
