package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageValues.Response.ResponseSuccess;
import Protocol.MessageValues.Room.OpenRoomsList;
import Protocol.MessageValues.Room.Room;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.DestinationsManager;
import com.example.clientgameapp.util.Converter;
import com.example.clientgameapp.util.StorageSingleton;
import com.example.clientgameapp.util.RoomCell;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import util.ErrorAlert;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class RoomLobbyController {
    public ListView<Room> roomsList;

    private ClientConnectionSingleton connection;
    private HighLevelMessageManager mManager;

    @FXML
    private ColorPicker gameColorPicker;
    private Socket socket;
    private Color color;

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
            roomsList.setItems(roomList);
            roomsList.setCellFactory(studentListView -> new RoomCell());


        } catch (ClientConnectionException | MismatchedClassException | BadResponseException ex) {
            System.out.println(ex.getMessage());
            ErrorAlert.show(ex.getMessage());
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            StorageSingleton.getInstance().getMainApp().closeGame();
        }
    }

    public void navigateRoomCreation(ActionEvent actionEvent) {
        destinationsManager.navigateRoomCreationScene();

    }

    public void navigateRoomList(ActionEvent actionEvent) {
        destinationsManager.navigateRoomListScene();
    }

    public void connectToRoom(ActionEvent actionEvent) {
        try {
            if (color == null) {
                throw new ClientException("You need to choose a color!");
            } else {
                Room selectedRoom = roomsList.getSelectionModel().getSelectedItems().get(0);
                if (selectedRoom != null) {
                    Room currentRoom = roomsList.getSelectionModel().getSelectedItems().get(0);
                    StorageSingleton storageSingleton = StorageSingleton.getInstance();
                    storageSingleton.setRoomId(currentRoom.getCode());
                    storageSingleton.setColor(color);
                    destinationsManager.navigateLobbyScene();
                    System.out.println(roomsList.getSelectionModel().getSelectedItems());
                } else {
                    throw new ClientException("You need to choose a room");
                }
            }
        } catch (ClientException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    public void getColor(ActionEvent actionEvent) {
        javafx.scene.paint.Color originalColor = gameColorPicker.getValue();
        color = Converter.getColor(originalColor);
    }

}
