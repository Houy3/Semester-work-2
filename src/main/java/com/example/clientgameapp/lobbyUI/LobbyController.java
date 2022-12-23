package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.ResponseError;
import Protocol.MessageValues.Response.ResponseSuccess;
import Protocol.MessageValues.Room.Room;
import Protocol.MessageValues.User.User;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.DestinationsManager;
import com.example.clientgameapp.models.UserModel;
import com.example.clientgameapp.util.ClientCell;
import com.example.clientgameapp.util.RoomCell;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.GameException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import util.ErrorAlert;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LobbyController {
    public ListView<UserModel> usersList;
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
            Message roomInfo = HighLevelMessageManager.getRoomParameters(socket);
            if (roomInfo.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) roomInfo.value();
                throw new GameException(error.getErrorMessage());
            } else {
                ResponseSuccess success = (ResponseSuccess) roomInfo.value();
                Room room = (Room) success.getResponseValue();
                List<UserModel> userModelList = new ArrayList<>();
                int i = 1;
                for (User user : room.getUsers()) {
                    UserModel model = new UserModel(
                            room.getUsersIsReady().get(user),
                            room.getUsersColor().get(user),
                            i,
                            user);
                    userModelList.add(model);
                    i++;
                }

                ObservableList<UserModel> list = FXCollections.observableArrayList();
                list.addAll(userModelList);
                usersList.setItems(list);
                usersList.setCellFactory(studentListView -> new ClientCell());
                System.out.println(list);
            }
        } catch (ClientConnectionException | MismatchedClassException | BadResponseException | IOException |
                 GameException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    public void setReadyStatus(ActionEvent actionEvent) {
    }

    public void startGame(ActionEvent actionEvent) {
    }

    public void updateLobby(ActionEvent actionEvent) {

    }
}
