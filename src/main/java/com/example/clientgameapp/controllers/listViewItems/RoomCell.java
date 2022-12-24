package com.example.clientgameapp.controllers.listViewItems;

import Protocol.MessageValues.Room.Room;
import com.example.clientgameapp.GameApp;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;

public class RoomCell extends ListCell<Room> {

    @FXML
    public Label labelAvailableSpacesCount;

    @FXML
    public Label labelGrowthRate;

    @FXML
    public Label labelArmySpeed;
    @FXML
    public Label labelCitiesCount;



    public FXMLLoader mLLoader;


    @FXML
    public BorderPane borderPane;


    @Override
    protected void updateItem(Room item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null || item.getGameInitializationForm() == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(GameApp.class.getResource("room-cell.fxml"));
                mLLoader.setController(this);
                System.out.println("HERE");
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            labelAvailableSpacesCount.setText("Space count: " + String.valueOf(item.getMaxCountOfPlayers() - item.getUsers().size()));
            labelArmySpeed.setText("Army speed: " + String.valueOf(item.getGameInitializationForm().getArmySpeed()));
            labelCitiesCount.setText("Cities count: " + String.valueOf(item.getGameInitializationForm().getCountOfCities()));
            labelGrowthRate.setText("Growth rate: " + String.valueOf(item.getGameInitializationForm().getArmyGrowthRate()));
            setText(null);
            setGraphic(borderPane);
        }
    }
}
