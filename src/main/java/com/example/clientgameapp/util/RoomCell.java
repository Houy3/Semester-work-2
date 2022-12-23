package com.example.clientgameapp.util;

import Protocol.MessageValues.Room.Room;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class RoomCell extends ListCell<Room> {

    @FXML
    private Label labelAvailableSpacesCount;

    @FXML
    private Label labelGrowthRate;

    @FXML
    private Label labelArmySpeed;
    @FXML
    private Label labelCitiesCount;

    public RoomCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("room-cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Room item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            labelAvailableSpacesCount.setText(String.valueOf(item.getMaxCountOfPlayers() - item.getUsers().size()));
            labelArmySpeed.setText(String.valueOf(item.getGameInitializationForm().getArmySpeed()));
            labelCitiesCount.setText(String.valueOf(item.getGameInitializationForm().getCountOfCities()));
            labelGrowthRate.setText(String.valueOf(item.getGameInitializationForm().getArmyGrowthRate()));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
