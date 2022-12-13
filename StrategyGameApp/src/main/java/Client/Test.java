package Client;

import Protocol.MessageValues.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            Map<Integer, String> map = new HashMap<>();
            map.put(12, "hohoh");
            map.put(15, "neet");
            User user = new User("gagaga", map);

            out.writeObject(user);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }
}
