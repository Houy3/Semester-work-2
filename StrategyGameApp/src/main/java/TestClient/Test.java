package TestClient;

import Protocol.HighLevelMessageManager;
import Protocol.Message.Request;
import Protocol.Message.RequestValues.GameInitializationForm;
import Protocol.Message.RequestValues.RoomConnectionForm;
import Protocol.Message.RequestValues.RoomInitializationForm;
import Protocol.Message.RequestValues.UserLoginForm;
import Protocol.Message.Response;
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




    public static void main(String[] args) throws IOException, ProtocolVersionException, InterruptedException {

        user1();
        user2();

        Response response = HighLevelMessageManager.startGame(
                socket1
        );
        System.out.println(response.type());
        System.out.println(response.value());

        System.out.println("read1");

        Request request = MessageManager.readRequest(socket1);
        System.out.println(request.type());
        System.out.println(request.value());

        System.out.println("end");

        System.out.println("read2");

        request = MessageManager.readRequest(socket2);
        System.out.println(request.type());
        System.out.println(request.value());


        Thread.sleep(3000);

        socket2.close();

        request = HighLevelMessageManager.readRequest(socket1);
        System.out.println(request.type());
        System.out.println(request.value());


        request = HighLevelMessageManager.readRequest(socket1);
        System.out.println(request.type());
        System.out.println(request.value());

        Thread.sleep(100000);

    }

    public static void user1() throws ProtocolVersionException, IOException {
        socket1 = new Socket("localhost", PORT);

        UserLoginForm user = new UserLoginForm("email@mail.ru", "password");

        Response response = HighLevelMessageManager.loginUser(
                user,
                socket1
        );
        System.out.println(response.type());
        System.out.println(response.value());

        RoomInitializationForm form = new RoomInitializationForm(
                4,
                RoomAccess.PUBLIC,
                Color.RED,
                new GameInitializationForm(10, 40, 3));

        response = HighLevelMessageManager.initializeRoom(form,
                socket1
        );
        code = ((Room)response.value()).code();
        System.out.println(response.type());
        System.out.println(response.value());

        response = HighLevelMessageManager.setUserReadyToStart(
                socket1
        );
        System.out.println(response.type());
        System.out.println(response.value());


    }

    public static void user2() throws ProtocolVersionException, IOException {

        socket2 = new Socket("localhost", PORT);

        UserLoginForm user = new UserLoginForm("email2@mail.ru", "password");

        Response response = HighLevelMessageManager.loginUser(
                user,
                socket2
        );
        System.out.println(response.type());
        System.out.println(response.value());

        RoomConnectionForm form = new RoomConnectionForm(
                code,
                Color.PINK);

        response = HighLevelMessageManager.connectToRoom(
                form,
                socket2
        );
        System.out.println(response.type());
        System.out.println(response.value());



        response = HighLevelMessageManager.setUserReadyToStart(
                socket2
        );
        System.out.println(response.type());
        System.out.println(response.value());

    }
}
