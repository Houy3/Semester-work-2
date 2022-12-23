package com.example.clientgameapp;

import exceptions.ClientConnectionException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.ErrorAlert;

import java.io.IOException;
import java.net.URL;

public class DestinationsManager {

    private Stage stage;
    private Scene scene;

    private static DestinationsManager instance;

    private DestinationsManager() {

    }

    static {
        try {
            instance = new DestinationsManager();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in creating singleton instance");
        }
    }

    public static DestinationsManager getInstance() throws ClientConnectionException {
        return instance;
    }

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void navigateChoiceScene()  {
        showScene(GameApp.class.getResource("choice-view.fxml"));
    }

    private void showScene(URL destiny) {
        try {
            Parent root = FXMLLoader.load(destiny);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    public void navigateLoginScene() {
        showScene(GameApp.class.getResource("login-view.fxml"));
    }

    public void navigateRegistrationScene() {
        showScene(GameApp.class.getResource("register-view.fxml"));
    }

    public void navigateRoomCreationScene() {
        showScene(GameApp.class.getResource("room-creation-view.fxml"));

    }

    public void navigateRoomListScene()  {
        showScene(GameApp.class.getResource("room-lobby-view.fxml"));

    }

    public void navigateProfileScene() {
        showScene(GameApp.class.getResource("profile-view.fxml"));

    }

    public void navigateLobbyScene()  {
        showScene(GameApp.class.getResource("lobby-view.fxml"));
    }

}
