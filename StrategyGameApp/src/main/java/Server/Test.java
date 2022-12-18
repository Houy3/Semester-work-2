package Server;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.Success;
import Protocol.MessageValues.User.UserRegistrationForm;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {

    private final static int PORT = 8080;
    public static void main(String[] args) {

        while (true) {
            Socket socket = null;
            try (ServerSocket server = new ServerSocket(PORT)) {
                socket = server.accept();

                //UserRegistrationForm user = new UserRegistrationForm("email", "nick", "pass");

                Message message = MessageManager.readMessage(socket.getInputStream());

                System.out.println(message);

                MessageManager.sendSuccessResponse(new Success(null), socket.getOutputStream());

            } catch (IOException | ProtocolVersionException | MismatchedClassException e) {
                System.out.println(e.getMessage());
                try {
                    assert socket != null;
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
