package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.Response.ResponseError;
import Protocol.MessageValues.Room.RoomAccess;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.DestinationsManager;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientException;
import exceptions.ClientInputException;
import exceptions.GameException;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.controlsfx.control.ToggleSwitch;
import util.ErrorAlert;
import utils.StringConverter;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class RoomCreationController {
    public ToggleSwitch togglePrivate;

    public ColorPicker gameColorPicker;
    public Spinner spinnerArmySpeed;
    public Spinner spinnerArmyGrowthRate;
    public Spinner spinnerCitiesAmount;

    private ClientConnectionSingleton connection;
    private HighLevelMessageManager mManager;
    private Socket socket;

    private Color color;
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

    public void createRoom(ActionEvent actionEvent) {
        new Thread(
                () -> {
                    try {
                        int citiesAmount = StringConverter.convertToInt((String) spinnerCitiesAmount.getValue());
                        int growthRate = StringConverter.convertToInt((String) spinnerArmyGrowthRate.getValue());
                        int armySpeed = StringConverter.convertToInt((String) spinnerArmySpeed.getValue());
                        boolean isPrivate = togglePrivate.isSelected();
                        RoomAccess access;
                        GameInitializationForm gameInitializationForm = new GameInitializationForm(
                                citiesAmount, growthRate, armySpeed
                        );
                        if (isPrivate) {
                            access = RoomAccess.PRIVATE;
                        } else {
                            access = RoomAccess.PUBLIC;
                        }
                        RoomInitializationForm newRoom = new RoomInitializationForm(
                                2, access, color, gameInitializationForm
                        );
                        Message roomInitializedMessage = HighLevelMessageManager.initializeRoom(newRoom, socket);
                        if (color == null) {
                            throw new ClientException("You need to choose a color");
                        }
                        if (roomInitializedMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                            ResponseError error = (ResponseError) roomInitializedMessage.value();
                            destinationsManager.navigateRoomListScene();
                            throw new GameException(error.getErrorMessage());
                        } else {
                            destinationsManager.navigateLobbyScene();
                        }
                    } catch (ClientException | ClientInputException | GameException | RuntimeException |
                             BadResponseException |
                             IOException |
                             MismatchedClassException e) {
                        ErrorAlert.show(e.getMessage());
                    }
                }
        ).start();
    }

    public void navigateProfile(ActionEvent actionEvent) {
        destinationsManager.navigateProfileScene();

    }

    public void navigateRoomList(ActionEvent actionEvent) {
        destinationsManager.navigateRoomListScene();

    }

    public void getColor(ActionEvent actionEvent) {
        javafx.scene.paint.Color originalColor = gameColorPicker.getValue();
        color = new Color(
                convertColorNumber(originalColor.getRed()),
                convertColorNumber(originalColor.getGreen()),
                convertColorNumber(originalColor.getBlue())
        );

    }

    private int convertColorNumber(Double num) {
        return (int) (num * 255);
    }
}
