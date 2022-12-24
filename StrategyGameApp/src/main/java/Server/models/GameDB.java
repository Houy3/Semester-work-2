package Server.models;

import Protocol.MessageValues.Game.Game;
import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.User.User;
import Protocol.MessageValues.models.CitiesMap.CitiesMap;
import Protocol.MessageValues.models.CitiesMap.City;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GameDB {

    private CitiesMap citiesMap;

    private Date startTime;


    private Map<City, Integer> citiesArmies;
    private Map<City, UserDB> usersCities;

    //Скорость армии, когда она идет от одного города к другому. усл.ед./с.
    private int armySpeed;

    //Скорость роста армии игрока в городе ед./с.
    private int armyGrowthRate;


    public GameDB(GameInitializationForm form) {
        this.armySpeed = form.getArmySpeed();
        this.armyGrowthRate = form.getArmyGrowthRate();
    }

    public Game toGame() {
        Game game = new Game();

        game.setCitiesMap(citiesMap);
        game.setStartTime(startTime);

        game.setCitiesArmies(citiesArmies);

        Map<City, User> usersCities = new HashMap<>();
        for (City city : this.usersCities.keySet()) {
            usersCities.put(city, this.usersCities.get(city).toUser());
        }
        game.setUsersCities(usersCities);

        game.setArmySpeed(armySpeed);
        game.setArmyGrowthRate(armyGrowthRate);

        return game;
    }

    public CitiesMap getCitiesMap() {
        return citiesMap;
    }

    public void setCitiesMap(CitiesMap citiesMap) {
        this.citiesMap = citiesMap;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Map<City, Integer> getCitiesArmies() {
        return citiesArmies;
    }

    public void setCitiesArmies(Map<City, Integer> citiesArmies) {
        this.citiesArmies = citiesArmies;
    }

    public Map<City, UserDB> getUsersCities() {
        return usersCities;
    }

    public void setUsersCities(Map<City, UserDB> usersCities) {
        this.usersCities = usersCities;
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
        return "GameDB{" +
                "citiesMap=" + citiesMap +
                ", startTime=" + startTime +
                ", citiesArmies=" + citiesArmies +
                ", usersCities=" + usersCities +
                ", armySpeed=" + armySpeed +
                ", armyGrowthRate=" + armyGrowthRate +
                '}';
    }
}
