package Protocol.MessageValues.Game;

import Protocol.MessageValues.MessageValue;

public final class GameInitializationForm implements MessageValue {

    private int countOfCities;

    //скорость армии, когда она идет от одного города к другому. усл.ед./с.
    private int armySpeed;

    //скорость роста армии игрока в городе
    private int armyGrowthRate;


    public GameInitializationForm(int countOfCities, int armySpeed, int armyGrowthRate) {
        this.countOfCities = countOfCities;
        this.armySpeed = armySpeed;
        this.armyGrowthRate = armyGrowthRate;
    }

    public int getCountOfCities() {
        return countOfCities;
    }

    public void setCountOfCities(int countOfCities) {
        this.countOfCities = countOfCities;
    }

    public int getArmySpeed() {
        return armySpeed;
    }

    public void setArmySpeed(int armySpeed) {
        this.armySpeed = armySpeed;
    }

    public int getArmyGrowthRate() {
        return armyGrowthRate;
    }

    public void setArmyGrowthRate(int armyGrowthRate) {
        this.armyGrowthRate = armyGrowthRate;
    }

    @Override
    public String toString() {
        return "GameInitializationForm{" +
                "countOfCities=" + countOfCities +
                ", armySpeed=" + armySpeed +
                ", armyGrowthRate=" + armyGrowthRate +
                '}';
    }
}
