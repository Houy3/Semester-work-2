package com.example.clientgameapp;

import com.example.clientgameapp.util.CommonValues;
import exceptions.ClientConnectionException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApp extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClientConnectionException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApp.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        CommonValues commonValues = CommonValues.getInstance();
        DestinationsManager destinationsManager = DestinationsManager.getInstance();
        destinationsManager.init(stage);
        stage.setTitle("Strategy Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}