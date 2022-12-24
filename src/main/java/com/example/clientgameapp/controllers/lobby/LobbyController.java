package com.example.clientgameapp.controllers.lobby;

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
import com.example.clientgameapp.controllers.listViewItems.ClientCell;
import util.Converter;
import com.example.clientgameapp.storage.StorageSingleton;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientException;
import exceptions.GameException;
import exceptions.ServerException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import com.example.clientgameapp.controllers.error.ErrorAlert;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LobbyController {
    public ListView<UserModel> usersList;
    public ColorPicker gameColorPicker;
    private ClientConnectionSingleton connection;
    private HighLevelMessageManager mManager;
    private Socket socket;
    private StorageSingleton storage;
    private DestinationsManager destinationsManager;

    private ScheduledExecutorService scheduler;

    private boolean isReady = false;


    public void initialize() {
        try {

            connection = ClientConnectionSingleton.getInstance();
            mManager = new HighLevelMessageManager();
            socket = connection.getSocket();
            destinationsManager = DestinationsManager.getInstance();
            storage = StorageSingleton.getInstance();
            storage.setLobbyController(this);
            scheduler = StorageSingleton.getInstance().getScheduler();
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
        } catch (MismatchedClassException | BadResponseException | ServerException ex) {
            ErrorAlert.show(ex.getMessage());
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            StorageSingleton.getInstance().getMainApp().closeGame();
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
        } catch (GameException | MismatchedClassException | BadResponseException ex) {
            ErrorAlert.show(ex.getMessage());
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            StorageSingleton.getInstance().getMainApp().closeGame();
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
            if (!isReady) {
                Message readyMessage = HighLevelMessageManager.readyToStart(socket);
                if (readyMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                    ResponseError error = (ResponseError) readyMessage.value();
                    throw new ServerException(error.getErrorMessage());
                } else {
                    ResponseSuccess success = (ResponseSuccess) readyMessage.value();
                    System.out.println(success.getResponseValue());
                }
                isReady = true;
            } else {
                Message readyMessage = HighLevelMessageManager.notReadyToStart(socket);
                if (readyMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                    ResponseError error = (ResponseError) readyMessage.value();
                    throw new ServerException(error.getErrorMessage());
                } else {
                    ResponseSuccess success = (ResponseSuccess) readyMessage.value();
                    System.out.println(success.getResponseValue());
                }
                isReady = false;
            }
        } catch (ServerException | MismatchedClassException | BadResponseException e) {
            ErrorAlert.show(e.getMessage());
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            StorageSingleton.getInstance().getMainApp().closeGame();
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
                scheduler.shutdownNow();
            }
        } catch (MismatchedClassException | BadResponseException | ServerException |
                 ClientException ex) {
            ErrorAlert.show(ex.getMessage());
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            StorageSingleton.getInstance().getMainApp().closeGame();
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

        } catch (ServerException | MismatchedClassException | BadResponseException e) {
            ErrorAlert.show(e.getMessage());
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            StorageSingleton.getInstance().getMainApp().closeGame();
        }
    }

    private void updateList() {
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

            } catch (ServerException | MismatchedClassException | BadResponseException e) {
                ErrorAlert.show(e.getMessage());
            } catch (IOException e) {
                ErrorAlert.show(e.getMessage());
                StorageSingleton.getInstance().getMainApp().closeGame();
            }
        };
        scheduler.scheduleAtFixedRate(helloRunnable, 0, 4, TimeUnit.SECONDS);
    }

    public void leaveLobby(ActionEvent actionEvent) {
        new Thread(
                () -> {
                    try {
                        Message message = HighLevelMessageManager.disconnectRoom(socket);
                        if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                            ResponseError error = (ResponseError) message.value();
                            throw new ServerException(error.getErrorMessage());
                        } else {
                            StorageSingleton.getInstance().nullifyAll();
                            StorageSingleton.getInstance().getScheduler().shutdownNow();
                            destinationsManager.navigateChoiceScene();
                        }
                    } catch (MismatchedClassException | BadResponseException | ServerException e) {
                        ErrorAlert.show(e.getMessage());
                    } catch (IOException e) {
                        ErrorAlert.show(e.getMessage());
                        StorageSingleton.getInstance().getMainApp().closeGame();
                    }
                }
        ).start();
    }

    public void changeColor(ActionEvent actionEvent) {
        try {
            javafx.scene.paint.Color originalColor = gameColorPicker.getValue();
            Color color = Converter.converColor(originalColor);
            Message message = HighLevelMessageManager.setColor(socket, color);
            if (message.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) message.value();
                throw new ServerException(error.getErrorMessage());
            } else {
                System.out.println((ResponseSuccess) message.value());
            }
        } catch (MismatchedClassException | BadResponseException | ServerException e) {
            ErrorAlert.show(e.getMessage());
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            StorageSingleton.getInstance().getMainApp().closeGame();
        }
    }
}
