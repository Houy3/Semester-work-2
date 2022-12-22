package com.example.clientgameapp.lobbyUI;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.Response.ErrorResponse;
import Protocol.MessageValues.Room.RoomAccess;
import Protocol.MessageValues.Room.RoomInitializationForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import com.example.clientgameapp.DestinationsManager;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientInputException;
import exceptions.ClientRegistrationException;
import exceptions.GameException;
import javafx.event.ActionEvent;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.controlsfx.control.ToggleSwitch;
import util.ErrorAlert;
import utils.StringConverter;

import java.io.IOException;
import java.net.Socket;

public class RoomCreationController {
    public Slider sliderPlayerCount;
    public TextField textFieldCitiesAmount;
    public TextField textFieldArmySpeed;
    public ToggleSwitch togglePrivate;
    public TextField textFieldArmyGrowthRate;

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

    public void createRoom(ActionEvent actionEvent) {
        new Thread(
                () -> {
                    try {
                        double maxPlayerCount = sliderPlayerCount.getValue();
                        int citiesAmount = StringConverter.convertToInt(textFieldCitiesAmount.getText());
                        int growthRate = StringConverter.convertToInt(textFieldArmyGrowthRate.getText());
                        int armySpeed = StringConverter.convertToInt(textFieldArmySpeed.getText());
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
                                (int) maxPlayerCount, access, gameInitializationForm
                        );
                        Message roomInitializedMessage = mManager.initializeRoom(newRoom, socket);
                        if (roomInitializedMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                            ErrorResponse error = (ErrorResponse) roomInitializedMessage.value();
                            destinationsManager.switchToRoomLobbyScene();
                            throw new GameException(error.getErrorMessage());
                        } else {
                            destinationsManager.switchToRoomLobbyScene();
                        }
                    } catch (ClientInputException | GameException | RuntimeException | BadResponseException |
                             IOException |
                             MismatchedClassException e) {
                        ErrorAlert.show(e.getMessage());
                    }
                }
        ).start();
    }
}
