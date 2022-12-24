package com.example.clientgameapp.util;

import javafx.event.ActionEvent;

import java.awt.*;

public class Converter {

    public static Color getColor(javafx.scene.paint.Color color) {
        return  new Color(
                convertColorNumber(color.getRed()),
                convertColorNumber(color.getGreen()),
                convertColorNumber(color.getBlue())
        );

    }

    private static int convertColorNumber(Double num) {
        return (int) (num * 255);
    }
}
