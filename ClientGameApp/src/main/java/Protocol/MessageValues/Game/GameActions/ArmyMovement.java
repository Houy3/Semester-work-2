package Protocol.MessageValues.Game.GameActions;

import Protocol.MessageValues.models.CitiesMap.Way;
import Protocol.MessageValues.MessageValue;

public final class ArmyMovement implements MessageValue {

    private Way way;

    private int armyCount;

    public ArmyMovement(Way way, int armyCount) {
        this.way = way;
        this.armyCount = armyCount;
    }

    public Way getWay() {
        return way;
    }

    public void setWay(Way way) {
        this.way = way;
    }

    public int getArmyCount() {
        return armyCount;
    }

    public void setArmyCount(int armyCount) {
        this.armyCount = armyCount;
    }

    @Override
    public String toString() {
        return "GameAction{" +
                "way=" + way +
                ", armyCount=" + armyCount +
                '}';
    }
}
