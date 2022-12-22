package com.example.clientgameapp;


import Protocol.MessageValues.Response.ErrorResponse;
import exceptions.ClientRegistrationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.User.UserRegistrationForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientEmptyFieldException;
import util.ErrorAlert;
import validators.Validator;

import java.io.IOException;
import java.net.Socket;

public class RegistrationController {
    public TextField textFieldEmail;
    public TextField textFieldNickName;
    public TextField textFieldPassword;

    private Stage stage;
    private Scene scene;

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


    public void registerUser(ActionEvent actionEvent) {
        try {
            String nickName = textFieldNickName.getText();
            String password = textFieldPassword.getText();
            String email = textFieldEmail.getText();
            System.out.println(nickName + password + email);
            if (Validator.isValid(nickName) && Validator.isValid(password) && Validator.isValid(email)) {
                UserRegistrationForm form = new UserRegistrationForm(
                        email, password, nickName
                );
                Message registerMessage = mManager.registerUser(form, socket);
                if (registerMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                    ErrorResponse error = (ErrorResponse) registerMessage.value();
                    throw new ClientRegistrationException(error.getErrorMessage());
                } else {
                    openLoginScene(actionEvent);
                }
            }
        } catch (MismatchedClassException | BadResponseException | IOException | ClientRegistrationException |
                 ClientEmptyFieldException e) {
            ErrorAlert.show(e.getMessage());
        }
    }


    public void openLoginScene(ActionEvent actionEvent) throws IOException {
        destinationsManager.openLoginScene(actionEvent);
    }
}