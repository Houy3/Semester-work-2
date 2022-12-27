package com.example.clientgameapp;

import Protocol.HighLevelMessageManager;
import Protocol.ProtocolVersionException;
import com.example.clientgameapp.storage.GlobalStorage;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;

public class GameApp extends Application {
    private Stage stage;

    private ClientConnectionSingleton connection;
    private DestinationsManager destinationsManager;
    private GlobalStorage globalStorage;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApp.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            globalStorage = GlobalStorage.getInstance();
            connection = ClientConnectionSingleton.getInstance();
            globalStorage.setScheduler(Executors.newScheduledThreadPool(1));
            destinationsManager = DestinationsManager.getInstance();
            destinationsManager.init(stage);
            globalStorage.setMainApp(this);
            this.stage = stage;
            stage.setTitle("Strategy Game");
            stage.setOnCloseRequest(we -> {
                closeGame();
                System.exit(0);
            });
            stage.setScene(scene);
            stage.show();
        } catch (ClientConnectionException | IOException e) {
            System.out.println();
        }

    }

    public void closeGame() {
        Platform.runLater(() -> {
            globalStorage.nullifyAll();
            globalStorage.getScheduler().shutdownNow();

            stage.close();
            try {
                connection.getSocketReceiver().close();
                connection.getSocketSender().close();
                HighLevelMessageManager.logoutUser(ClientConnectionSingleton.getInstance().getSocketSender());
                HighLevelMessageManager.exit(ClientConnectionSingleton.getInstance().getSocketSender());
                ClientConnectionSingleton.getInstance().getSocketSender().close();
            } catch (IOException | ClientConnectionException |
                     ProtocolVersionException e) {
                System.out.println(e.getMessage());
            }
        });

    }

    public static void main(String[] args) {
        launch();
    }
}