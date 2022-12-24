package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Response.ResponseError;
import Protocol.MessageValues.Response.ResponseSuccess;
import Protocol.MessageValues.Room.Room;
import Protocol.MessageValues.Room.RoomConnectionForm;
import Protocol.MessageValues.User.User;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.DestinationsManager;
import com.example.clientgameapp.models.UserModel;
import com.example.clientgameapp.util.ClientCell;
import com.example.clientgameapp.util.RoomCell;
import com.example.clientgameapp.util.StorageSingleton;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientException;
import exceptions.GameException;
import exceptions.ServerException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import util.ErrorAlert;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LobbyController {
    public ListView<UserModel> usersList;
    private ClientConnectionSingleton connection;
    private HighLevelMessageManager mManager;
    private Socket socket;
    private StorageSingleton storage;
    private DestinationsManager destinationsManager;


    public void initialize() {
        try {
            connection = ClientConnectionSingleton.getInstance();
            mManager = new HighLevelMessageManager();
            socket = connection.getSocket();
            destinationsManager = DestinationsManager.getInstance();
            storage = StorageSingleton.getInstance();
            if (storage.getColor() != null && storage.getRoomId() != null) {
                initializeExistingRoom();
            } else {
                initializeNewRoom();
            }
            updateList();
        } catch (ClientConnectionException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    private void initializeExistingRoom() {
        try {
            RoomConnectionForm connectionForm = new RoomConnectionForm(
                    storage.getRoomId(), storage.getColor()
            );
            Message existingRoom = HighLevelMessageManager.connectRoom(
                    connectionForm, socket
            );
            if (existingRoom.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) existingRoom.value();
                throw new ServerException(error.getErrorMessage());
            } else {
                initializeList(existingRoom);
            }
        } catch (IOException | MismatchedClassException | BadResponseException | ServerException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    private void initializeNewRoom() {
        try {
            Message newRoom = HighLevelMessageManager.getRoomParameters(socket);
            if (newRoom.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) newRoom.value();
                throw new GameException(error.getErrorMessage());
            } else {
                initializeList(newRoom);
            }
        } catch (IOException | GameException | MismatchedClassException | BadResponseException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    private void initializeList(Message roomValue) {
        ResponseSuccess success = (ResponseSuccess) roomValue.value();
        Room currentRoom = (Room) success.getResponseValue();
        List<UserModel> userModelList = new ArrayList<>();
        int i = 1;
        for (User user : currentRoom.getUsers()) {
            UserModel model = new UserModel(
                    currentRoom.getUsersIsReady().get(user),
                    currentRoom.getUsersColor().get(user),
                    i,
                    user);
            userModelList.add(model);
            i++;
        }
        ObservableList<UserModel> list = FXCollections.observableArrayList();
        list.addAll(userModelList);
        usersList.setItems(list);
        usersList.setCellFactory(studentListView -> new ClientCell());
    }

    public void setReadyStatus(ActionEvent actionEvent) {
        try {
            Message readyMessage = HighLevelMessageManager.readyToStart(socket);
            if (readyMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) readyMessage.value();
                throw new ServerException(error.getErrorMessage());
            } else {
                ResponseSuccess success = (ResponseSuccess) readyMessage.value();
                System.out.println(success.getResponseValue());
            }
        } catch (Exception ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    public void startGame(ActionEvent actionEvent) {
        try {
            Message gameStart = HighLevelMessageManager.getRoomParameters(socket);
            if (gameStart.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) gameStart.value();
                throw new ServerException(error.getErrorMessage());
            } else {
                ResponseSuccess success = (ResponseSuccess) gameStart.value();
                Room room = (Room) success.getResponseValue();
                for (Object userStatus : room.getUsersIsReady().values().toArray()) {
                    if (!(boolean) userStatus) {
                        throw new ClientException("Not all users are ready");
                    }
                }
                destinationsManager.navigateGameScene();
                System.out.println(success.getResponseValue());
            }
        } catch (IOException | MismatchedClassException | BadResponseException | ServerException |
                 ClientException ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }

    public void updateLobby(ActionEvent actionEvent) {
        System.out.println("RUNNING");
        try {
            Message updatedData = HighLevelMessageManager.getRoomParameters(socket);
            if (updatedData.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) updatedData.value();
                throw new ServerException(error.getErrorMessage());
            } else {
                initializeList(updatedData);
                System.out.println(updatedData.type());
            }

        } catch (ServerException | MismatchedClassException | BadResponseException | IOException e) {
            ErrorAlert.show(e.getMessage());
        }
    }

    private void updateList() {
        try {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            Runnable helloRunnable = () -> {
                try {
                    Message updatedData = HighLevelMessageManager.getRoomParameters(socket);
                    if (updatedData.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                        ResponseError error = (ResponseError) updatedData.value();
                        throw new ServerException(error.getErrorMessage());
                    } else {
                        Platform.runLater(() -> {
                            initializeList(updatedData);
                        });
                        System.out.println(updatedData.type());
                    }

                } catch (ServerException | MismatchedClassException | BadResponseException | IOException e) {
                    ErrorAlert.show(e.getMessage());
                }
            };
            executor.scheduleAtFixedRate(helloRunnable, 0, 4, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ErrorAlert.show(ex.getMessage());
        }
    }
}
