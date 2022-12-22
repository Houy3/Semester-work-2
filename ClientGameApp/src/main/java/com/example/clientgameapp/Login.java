package com.example.clientgameapp;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.ErrorResponse;
import Protocol.MessageValues.User.UserLoginForm;
import com.example.clientgameapp.GameApp;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientRegistrationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.ErrorAlert;
import validators.Validator;

import java.io.IOException;
import java.net.Socket;

public class Login {
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;

    private Stage stage;
    private Scene scene;
    private Parent root;


    private ClientConnectionSingleton connection;
    private HighLevelMessageManager mManager;
    private Socket socket;

    private DestinationsManager destinationsManager;


    public void initialize() {
        try {
            connection = ClientConnectionSingleton.getInstance();
            mManager = new HighLevelMessageManager();
            socket = connection.getSocket();
            destinationsManager = new DestinationsManager();
        } catch (ClientConnectionException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }


    public void loginUser(ActionEvent actionEvent) {

        try {
            String email = textFieldEmail.getText();
            String password = textFieldPassword.getText();
            if (Validator.isValid(email) && Validator.isValid(password)) {
                UserLoginForm loginForm = new UserLoginForm(
                        email, password
                );
                Message userLogin = mManager.loginUser(
                        loginForm, socket
                );
                if (userLogin.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                    ErrorResponse error = (ErrorResponse) userLogin.value();
                    throw new ClientRegistrationException(error.getErrorMessage());
                } else {
                    switchToChoiceScene(actionEvent);
                }
            }
        } catch (Exception ex) {
            ErrorAlert.show(ex.getMessage());

        }
    }

    public void switchToRegistration(ActionEvent actionEvent) throws IOException {
        destinationsManager.switchToRegistration(actionEvent);
    }


    public void switchToChoiceScene(ActionEvent actionEvent) throws IOException {
        destinationsManager.switchToChoiceScene(actionEvent);
    }
}
