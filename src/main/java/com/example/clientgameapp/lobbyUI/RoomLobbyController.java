package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageValues.Response.ResponseSuccess;
import Protocol.MessageValues.Room.OpenRoomsList;
import Protocol.MessageValues.Room.Room;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.DestinationsManager;
import com.example.clientgameapp.util.RoomCellFactory;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.ErrorAlert;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class RoomLobbyController {
    public ListView<Room> roomsList;

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
            Message message = HighLevelMessageManager.getOpenRooms(socket);
            ResponseSuccess responseSuccess =(ResponseSuccess) message.value();
            OpenRoomsList list = (OpenRoomsList) responseSuccess.getResponseValue();
            List<Room> rooms = list.getOpenRooms();
            roomsList.setCellFactory(param -> new ListCell<Room>() {

                private final Label labelAvailableSpacesCount = new Label("ImageURL");
                private final Label labelGrowthRate = new Label("Text");
                private final Label labelArmySpeed = new Label("Button");
                private final Label labelCitiesCount = new Label("text");
                private final BorderPane layout = new BorderPane(labelCitiesCount, labelArmySpeed, labelGrowthRate, labelAvailableSpacesCount, null);

                @Override
                protected void updateItem(Room item, boolean empty) {

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        labelAvailableSpacesCount.setText(String.valueOf(item.getMaxCountOfPlayers() - item.getUsers().size()));
                        labelArmySpeed.setText(String.valueOf(item.getGameInitializationForm().getArmySpeed()));
                        labelCitiesCount.setText(String.valueOf(item.getGameInitializationForm().getCountOfCities()));
                        labelGrowthRate.setText(String.valueOf(item.getGameInitializationForm().getArmyGrowthRate()));
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        setGraphic(layout);
                    }
                }
            });

            for (Room room : rooms) {
                roomsList.getItems().add(room);
            }
        } catch (ClientConnectionException | MismatchedClassException | BadResponseException | IOException ex) {
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
