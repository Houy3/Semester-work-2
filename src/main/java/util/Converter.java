package util;

import exceptions.ClientInputException;

import java.awt.*;

public class Converter {

    public static Color converColor(javafx.scene.paint.Color color) {
        return  new Color(
                convertColorNumber(color.getRed()),
                convertColorNumber(color.getGreen()),
                convertColorNumber(color.getBlue())
        );

    }

    public static int convertToInt(String text) throws ClientInputException {
        if (text.isEmpty() || text.isBlank()) {
            throw new ClientInputException("Field can not be empty");
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            throw new ClientInputException("Field must be numerical");
        }
    }

    private static int convertColorNumber(Double num) {
        return (int) (num * 255);
    }
}
