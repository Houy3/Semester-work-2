package com.example.clientgameapp.userUI;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.ResponseError;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.DestinationsManager;
import com.example.clientgameapp.util.StorageSingleton;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ServerException;
import javafx.event.ActionEvent;
import util.ErrorAlert;

import java.io.IOException;
import java.net.Socket;

public class ProfileController {

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

    public void navigateToRoomList(ActionEvent actionEvent) {
        destinationsManager.navigateRoomListScene();
    }

    public void navigateToRoomCreation(ActionEvent actionEvent) {
        destinationsManager.navigateRoomCreationScene();
    }

    public void logout(ActionEvent actionEvent) {
        try {
            Message logoutMessage = HighLevelMessageManager.logoutUser(socket);
            if(logoutMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) logoutMessage.value();
                throw new ServerException(error.getErrorMessage());
            } else {
                destinationsManager.navigateLoginScene();
            }
        } catch (ServerException | MismatchedClassException | BadResponseException e) {
            ErrorAlert.show(e.getMessage());
        }  catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            StorageSingleton.getInstance().getMainApp().closeGame();
        }
    }
}
