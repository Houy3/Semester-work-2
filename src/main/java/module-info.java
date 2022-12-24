module com.example.clientgameapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.clientgameapp to javafx.fxml;
    exports com.example.clientgameapp;
    exports com.example.clientgameapp.controllers.user;
    exports Protocol.MessageValues.Room;
    exports com.example.clientgameapp.models;
    exports Protocol.MessageValues.User;
    exports Protocol.MessageValues.Response;
    exports Protocol.MessageValues.Game;
    exports util;
    exports com.example.clientgameapp.controllers.error;
    opens com.example.clientgameapp.controllers.user to javafx.fxml;
    exports com.example.clientgameapp.controllers.lobby;
    opens com.example.clientgameapp.controllers.lobby to javafx.fxml;
    exports com.example.clientgameapp.controllers.game;
    opens com.example.clientgameapp.controllers.game to javafx.fxml;
    exports com.example.clientgameapp.controllers.listViewItems;
    exports com.example.clientgameapp.storage;
}