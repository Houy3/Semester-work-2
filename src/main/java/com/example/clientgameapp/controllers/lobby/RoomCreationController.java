package com.example.clientgameapp.controllers.lobby;

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
import com.example.clientgameapp.storage.GlobalStorage;
import util.Converter;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import exceptions.ClientException;
import exceptions.GameException;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import org.controlsfx.control.ToggleSwitch;
import com.example.clientgameapp.controllers.error.ErrorAlert;

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

        try {
            System.out.println(socket.isClosed());
            int citiesAmount = (int) spinnerCitiesAmount.getValue();
            int growthRate = (int) spinnerArmyGrowthRate.getValue();
            int armySpeed = (int) spinnerArmySpeed.getValue();
            boolean isPrivate = togglePrivate.isSelected();
            RoomAccess access;
            if (color == null) {
                throw new ClientException("You need to choose a color");
            }
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

            if (roomInitializedMessage.type() == MessageManager.MessageType.RESPONSE_ERROR) {
                ResponseError error = (ResponseError) roomInitializedMessage.value();
                throw new GameException(error.getErrorMessage());
            } else {
                GlobalStorage.getInstance().getScheduler().shutdownNow();
                GlobalStorage.getInstance().setColor(color);
                destinationsManager.navigateLobbyScene();
            }
        } catch (ClientException | GameException | RuntimeException |
                 BadResponseException |
                 MismatchedClassException e) {
            ErrorAlert.show(e.getMessage());
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            GlobalStorage.getInstance().getMainApp().closeGame();
        }
    }

    public void navigateProfile(ActionEvent actionEvent) {
        destinationsManager.navigateProfileScene();

    }

    public void navigateRoomList(ActionEvent actionEvent) {
        destinationsManager.navigateRoomListScene();
    }

    public void getColor(ActionEvent actionEvent) {
        javafx.scene.paint.Color originalColor = gameColorPicker.getValue();
        color = Converter.convertColor(originalColor);
    }
}
