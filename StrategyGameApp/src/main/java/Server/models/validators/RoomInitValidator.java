package Server.models.validators;

import Protocol.MessageValues.Room.RoomInitializationForm;

public record RoomInitValidator(int minCountOfPlayers, int maxCountOfPlayers, GameInitValidator gameInitValidator) implements Validator<RoomInitializationForm> {

    @Override
    public void check(RoomInitializationForm object) throws IllegalArgumentException, ValidatorException {
        if (object == null) {
            throw new ValidatorException("Room initialization form is empty. ");
        }

        if (object.getAccess() == null) {
            throw new ValidatorException("Set access on room. ");
        }

        if (object.getCreatorColor() == null) {
            throw new ValidatorException("Choose your color in game. ");
        }

        if (object.getMaxCountOfPlayers() < minCountOfPlayers || object.getMaxCountOfPlayers() > maxCountOfPlayers) {
            throw new ValidatorException("Minimum " + minCountOfPlayers + " players. Maximum " + maxCountOfPlayers + " players ");
        }

        gameInitValidator.check(object.getGameInitializationForm());
    }
}
