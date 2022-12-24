package com.example.clientgameapp;

import Protocol.HighLevelMessageManager;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.util.StorageSingleton;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.ErrorAlert;

import java.io.IOException;
import java.util.concurrent.Executors;

public class GameApp extends Application {
    private Stage stage;

    private DestinationsManager destinationsManager;
    private StorageSingleton storageSingleton;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApp.class.getResource("register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            storageSingleton = StorageSingleton.getInstance();
            storageSingleton.setScheduler(Executors.newScheduledThreadPool(1));
            destinationsManager = DestinationsManager.getInstance();
            destinationsManager.init(stage);
            storageSingleton.setMainApp(this);
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
        storageSingleton.nullifyAll();
        storageSingleton.getScheduler().shutdownNow();
        stage.close();
        try {
            HighLevelMessageManager.exit(ClientConnectionSingleton.getInstance().getSocket());
            ClientConnectionSingleton.getInstance().getSocket().close();
        } catch (IOException | ClientConnectionException | MismatchedClassException | BadResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}