package utils;

import exceptions.ClientInputException;

public class StringConverter {
    public static boolean isValid(String text) throws ClientInputException {
        if (text.isEmpty() || text.isBlank()) {
            throw new ClientInputException("Must fill in all the fields");
        }
        return true;
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
}
