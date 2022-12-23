package com.example.clientgameapp.util;

import Protocol.MessageValues.User.User;
import com.example.clientgameapp.GameApp;
import com.example.clientgameapp.models.UserModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.IOException;

public class ClientCell extends ListCell<UserModel> {

    @FXML
    public Label labelUserId;

    @FXML
    public Label labelUserName;

    @FXML
    public Label labelColor;
    @FXML
    public Label labelStatus;


    public FXMLLoader mLLoader;


    @FXML
    public BorderPane borderPane;


    @Override
    protected void updateItem(UserModel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(GameApp.class.getResource("user-cell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            labelUserId.setText(String.valueOf(item.getIndex()));
            labelColor.setStyle("-fx-background-color: rgb(" + item.getUserColor().getRed() + ","
                    + item.getUserColor().getGreen() + "," + item.getUserColor().getBlue() + ");");
            labelStatus.setText("Ready: " + String.valueOf(item.getUserStatus()));
            labelUserName.setText(String.valueOf(item.getUser().getNickname()));
            setText(null);
            setGraphic(borderPane);
        }
    }
}
