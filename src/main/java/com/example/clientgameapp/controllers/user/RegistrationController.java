package com.example.clientgameapp.controllers.user;


import Protocol.MessageValues.Response.ResponseError;
import com.example.clientgameapp.DestinationsManager;
import com.example.clientgameapp.GameApp;
import com.example.clientgameapp.storage.GlobalStorage;
import exceptions.ClientException;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.User.UserRegistrationForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientInputException;
import com.example.clientgameapp.controllers.error.ErrorAlert;
import utils.Validator;

import java.io.IOException;
import java.net.Socket;

public class RegistrationController {
    public TextField textFieldEmail;
    public TextField textFieldNickName;
    public TextField textFieldPassword;

    private GameApp gameApp;
    private Scene scene;

    private ClientConnectionSingleton connection;
    private HighLevelMessageManager mManager;
    private Socket socket;

    private DestinationsManager destinationsManager;

    public void initialize() {
        try {
            gameApp = GlobalStorage.getInstance().getMainApp();
            connection = ClientConnectionSingleton.getInstance();
            mManager = new HighLevelMessageManager();
            socket = connection.getSocket();
            destinationsManager = DestinationsManager.getInstance();
        } catch (ClientConnectionException ex) {
            ErrorAlert.show(ex.getMessage());
            gameApp.closeGame();
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
                    ResponseError error = (ResponseError) registerMessage.value();
                    throw new ClientException(error.getErrorMessage());
                } else {
                    openLoginScene(actionEvent);
                }
            }
        } catch (MismatchedClassException | BadResponseException  | ClientException |
                 ClientInputException e) {
            ErrorAlert.show(e.getMessage());
        } catch (IOException ex) {
            GlobalStorage.getInstance().getMainApp().closeGame();
            ErrorAlert.show(ex.getMessage());
        }
    }


    public void openLoginScene(ActionEvent actionEvent) throws IOException {
        destinationsManager.navigateLoginScene();
    }
}