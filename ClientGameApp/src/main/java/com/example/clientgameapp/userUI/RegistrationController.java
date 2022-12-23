package com.example.clientgameapp.userUI;


import Protocol.MessageValues.Response.ResponseError;
import com.example.clientgameapp.DestinationsManager;
import exceptions.ClientException;
import javafx.event.ActionEvent;
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
import exceptions.ClientInputException;
import util.ErrorAlert;
import utils.StringConverter;

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
            destinationsManager = DestinationsManager.getInstance();
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
            if (StringConverter.isValid(nickName) && StringConverter.isValid(password) && StringConverter.isValid(email)) {
                UserRegistrationForm form = new UserRegistrationForm(
                        email, password, nickName
                );
                Message registerMessage = mManager.registerUser(form, socket);
                if (registerMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                    ResponseError error = (ResponseError) registerMessage.value();
                    throw new ClientException(error.getErrorMessage());
                } else {
                    openLoginScene(actionEvent);
                }
            }
        } catch (MismatchedClassException | BadResponseException | IOException | ClientException |
                 ClientInputException e) {
            ErrorAlert.show(e.getMessage());
        }
    }


    public void openLoginScene(ActionEvent actionEvent) throws IOException {
        destinationsManager.navigateLoginScene();
    }
}