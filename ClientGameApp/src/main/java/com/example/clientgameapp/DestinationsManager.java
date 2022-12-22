package com.example.clientgameapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DestinationsManager {

    private static Stage stage;
    private Scene scene;

    public void switchToChoiceScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(GameApp.class.getResource("creation-room-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openLoginScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(GameApp.class.getResource("login-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRegistration(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(GameApp.class.getResource("register-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
