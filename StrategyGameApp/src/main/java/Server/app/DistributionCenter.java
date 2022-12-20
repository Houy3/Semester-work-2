package Server.app;

import Server.DB.DataSources.SimpleDataSource;
import Server.services.ServicesToolKit;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class DistributionCenter implements Runnable {

    private final ServerSocket server;

    private ServicesToolKit servicesToolKit;

    public DistributionCenter(ServerSocket server) {
        this.server = server;
        init();
    }


    public void init() {
        Properties properties = new Properties();
        try {
            properties.load(ServerApp.class.getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        DataSource dataSource = new SimpleDataSource(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );

        servicesToolKit = new ServicesToolKit(dataSource);
    }


    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = server.accept();
                new Thread(new UserConnectionThread(socket, servicesToolKit)).start();
            }
        } catch (IOException e) {
            ErrorLogger.log(e.getMessage());
        }
    }
}
