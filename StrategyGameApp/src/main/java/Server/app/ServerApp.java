package Server.app;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {

    public static void main(String[] args) {
        try {
            ServerApp serverApp = new ServerApp(8888);
            serverApp.start();
        } catch (Exception e) {
            ErrorLogger.log(e.getMessage());
        }
    }

    public ServerApp(int port) {
        this.port = port;
    }

    private final int port;

    public void start() {
        try {
            ServerSocket server = new ServerSocket(port);
            new Thread(new DistributionCenter(server)).start();
        } catch (IOException e) {
            ErrorLogger.log(e.getMessage());
        }
    }

}
