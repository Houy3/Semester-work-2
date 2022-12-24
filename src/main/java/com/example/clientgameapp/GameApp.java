package com.example.clientgameapp;

import com.example.clientgameapp.util.StorageSingleton;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.ErrorAlert;

import java.io.IOException;
import java.util.concurrent.Executors;

public class GameApp extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApp.class.getResource("register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            Font.loadFont(GameApp.class.getResource("font/RUBIK.TTF").toExternalForm(), 10);
            StorageSingleton storageSingleton = StorageSingleton.getInstance();
            storageSingleton.setScheduler(Executors.newScheduledThreadPool(1));
            DestinationsManager destinationsManager = DestinationsManager.getInstance();
            destinationsManager.init(stage);
            stage.setTitle("Strategy Game");
            stage.setOnCloseRequest(we -> {
                storageSingleton.nullifyAll();
                storageSingleton.getScheduler().shutdownNow();
                try {
                    ClientConnectionSingleton.getInstance().getSocket().close();
                    stage.close();
                    System.exit(0);
                } catch (IOException | ClientConnectionException e) {
                    throw new RuntimeException(e);
                }
            });
            stage.setScene(scene);
            stage.show();
        } catch (ClientConnectionException | IOException e) {
            ErrorAlert.show(e.getMessage());
        }

    }

    public static void main(String[] args) {
        launch();
    }
}