package validators;

import exceptions.ClientEmptyFieldException;

public class Validator {
    public static boolean isValid(String text) throws ClientEmptyFieldException {
        if(text.isEmpty() || text.isBlank()) {
            throw new ClientEmptyFieldException("Must fill in all the fields");
        }
        return true;
    }
}
