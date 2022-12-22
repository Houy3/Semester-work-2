module ru.kpfu.itis.clientapp {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens ru.kpfu.itis.clientapp to javafx.fxml;
    exports ru.kpfu.itis.clientapp;
}