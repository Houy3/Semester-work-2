package com.example.clientgameapp.userUI;

import Protocol.HighLevelMessageManager;
import com.example.clientgameapp.DestinationsManager;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
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
}
