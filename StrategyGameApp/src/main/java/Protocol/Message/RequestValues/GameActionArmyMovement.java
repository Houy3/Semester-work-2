package Protocol.Message.RequestValues;

import Protocol.Message.models.Way;

public record GameActionArmyMovement(Way way, int armyCount) implements RequestValue {

    @Override
    public String toString() {
        return "GameAction{" +
                "way=" + way +
                ", armyCount=" + armyCount +
                '}';
    }
}
