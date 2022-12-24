package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import com.example.clientgameapp.DestinationsManager;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.event.ActionEvent;
import util.ErrorAlert;

import java.io.IOException;
import java.net.Socket;

public class ChoiceController {

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

    public void connectToRoom(ActionEvent actionEvent) {
        destinationsManager.navigateRoomListScene();

    }

    public void createRoom(ActionEvent actionEvent) {
        destinationsManager.navigateRoomCreationScene();

    }

    public void openProfile(ActionEvent actionEvent) {
        destinationsManager.navigateProfileScene();
    }
}
