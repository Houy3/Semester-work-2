package Server;

import Protocol.MessageValues.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Test{

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket socket = serverSocket.accept();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            User user = (User) in.readObject();
            System.out.println(user);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
