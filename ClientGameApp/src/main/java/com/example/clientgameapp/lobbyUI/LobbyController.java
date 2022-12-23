package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import com.example.clientgameapp.DestinationsManager;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import util.ErrorAlert;

import java.net.Socket;

public class LobbyController {
    public ListView usersList;
    public Label labelRoomCode;
    public Label labelCountOfPlayers;
    public Label labelStatus;
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
