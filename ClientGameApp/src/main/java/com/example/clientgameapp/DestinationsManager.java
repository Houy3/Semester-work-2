package com.example.clientgameapp;

import Protocol.Message;
import Protocol.MessageManager;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;

public class DestinationsManager {

    private  Stage stage;
    private  Scene scene;

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

    public void init(Stage stage) throws ClientConnectionException {
       this.stage = stage;
    }

    public void switchToChoiceScene() throws IOException {
        showScene(GameApp.class.getResource("choice-view.fxml"));
    }

    private void showScene(URL destiny) throws IOException {
        Parent root = FXMLLoader.load(destiny);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoginScene() throws IOException {
        showScene(GameApp.class.getResource("login-view.fxml"));
    }

    public void switchToRegistration() throws IOException {
        showScene(GameApp.class.getResource("register-view.fxml"));
    }

    public void switchToRoomCreationScene() throws IOException {
        showScene(GameApp.class.getResource("room-creation-view.fxml"));
    }

    public void switchToRoomLobbyScene() throws IOException {
        showScene(GameApp.class.getResource("room-lobby-view.fxml"));
    }

    public void switchToProfileScene() throws IOException {
        showScene(GameApp.class.getResource("profile-view.fxml"));
    }

    public void switchToLobbyScene() throws IOException {
        showScene(GameApp.class.getResource("lobby-view.fxml"));
    }

}
