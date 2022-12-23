package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import com.example.clientgameapp.DestinationsManager;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import util.ErrorAlert;

import java.io.IOException;
import java.net.Socket;

public class RoomLobbyController {
    public ListView<String> roomsList;

    public String[] rooms = new String[]{
            "Room 1", "Room 2", "Room 3"
    };
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
            for (int i = 0; i < rooms.length; i++) {
                roomsList.getItems().add(rooms[i]);
            }
        } catch (ClientConnectionException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    public void navigateRoomCreation(ActionEvent actionEvent) {
        destinationsManager.navigateRoomCreationScene();

    }

    public void navigateRoomList(ActionEvent actionEvent) {
        destinationsManager.navigateRoomListScene();

    }

    public void connectToRoom(ActionEvent actionEvent) {
        destinationsManager.navigateLobbyScene();
    }
}
