module com.example.clientgameapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.clientgameapp to javafx.fxml;
    exports com.example.clientgameapp;
    exports com.example.clientgameapp.userUI;
    exports Protocol.MessageValues.Room;
    exports com.example.clientgameapp.models;
    exports Protocol.MessageValues.User;
    exports Protocol.MessageValues.Response;
    exports Protocol.MessageValues.Game;
    exports util;
    exports com.example.clientgameapp.util;
    opens com.example.clientgameapp.userUI to javafx.fxml;
    exports com.example.clientgameapp.lobbyUI;
    opens com.example.clientgameapp.lobbyUI to javafx.fxml;
}