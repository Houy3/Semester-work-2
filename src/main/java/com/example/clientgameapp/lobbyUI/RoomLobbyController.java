package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageValues.Response.ResponseSuccess;
import Protocol.MessageValues.Room.OpenRoomsList;
import Protocol.MessageValues.Room.Room;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.DestinationsManager;
import com.example.clientgameapp.util.RoomCell;
import com.example.clientgameapp.util.RoomCellFactory;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.ErrorAlert;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class RoomLobbyController {
    public ListView<Room> roomsList;

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
            Message message = HighLevelMessageManager.getOpenRooms(socket);
            ResponseSuccess responseSuccess = (ResponseSuccess) message.value();
            OpenRoomsList list = (OpenRoomsList) responseSuccess.getResponseValue();
            List<Room> rooms = list.getOpenRooms();
            ObservableList<Room> roomList = FXCollections.observableArrayList();
            roomList.addAll(rooms);
            System.out.println(roomList + " " + rooms);
            roomsList.setItems(roomList);
            roomsList.setCellFactory(studentListView -> new RoomCell());


        } catch (ClientConnectionException | MismatchedClassException | BadResponseException | IOException ex) {
            System.out.println(ex.getMessage());
            ErrorAlert.show(ex.getMessage());
        }
    }

    public void navigateRoomCreation(ActionEvent actionEvent) {
        destinationsManager.navigateRoomCreationScene();

    }

    public void navigateRoomList(ActionEvent actionEvent) {
        destinationsManager.navigateRoomListScene();

    }

    public void connectToRoom(ActionEvent actionEvent) throws MismatchedClassException, BadResponseException, IOException {

        //destinationsManager.navigateLobbyScene();
    }
}
