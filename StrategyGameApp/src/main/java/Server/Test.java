package Server;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.Success;
import Protocol.MessageValues.User.User;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private final static int PORT = 8080;
    public static void main(String[] args) {

        while (true) {
            try (ServerSocket server = new ServerSocket(PORT)) {
                Socket socket = server.accept();

                User user = new User("email", "nick", "pass");

                Message message = MessageManager.readMessage(socket.getInputStream());

                System.out.println(message);

                MessageManager.sendSuccessResponse(new Success(), socket.getOutputStream());

            } catch (IOException | ProtocolVersionException | MismatchedClassException e) {
                e.printStackTrace();
            }
        }
    }
}
