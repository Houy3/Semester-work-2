package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import com.example.clientgameapp.DestinationsManager;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import util.ErrorAlert;

import java.net.Socket;

public class RoomLobbyController {
    public ListView<String> roomsList;

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
}
