package Protocol.MessageValues.Game;

import Protocol.MessageValues.Game.CitiesMap.CitiesMap;
import Protocol.MessageValues.Game.CitiesMap.City;
import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.UserPublicData;

import java.awt.*;
import java.util.Date;
import java.util.Map;

public final class Game implements MessageValue {

    private CitiesMap citiesMap;
    private Map<UserPublicData, Color> usersColor;
    private Date startTime;



    private Map<City, Integer> citiesArmies;
    private Map<City, UserPublicData> usersCities;



    //скорость армии, когда она идет от одного города к другому. усл.ед./с.
    private int armySpeed;

    //скорость роста армии игрока в городе ед./с.
    private int armyGrowthRate;

    public Game() {
    }

    public Game(CitiesMap citiesMap, Map<City, Integer> citiesArmies, Map<City, UserPublicData> usersCities, Map<UserPublicData, Color> usersColor, Date startTime, int armySpeed, int armyGrowthRate) {
        this.citiesMap = citiesMap;
        this.citiesArmies = citiesArmies;
        this.usersCities = usersCities;
        this.usersColor = usersColor;
        this.startTime = startTime;
        this.armySpeed = armySpeed;
        this.armyGrowthRate = armyGrowthRate;
    }


    public CitiesMap getCitiesMap() {
        return citiesMap;
    }

    public void setCitiesMap(CitiesMap citiesMap) {
        this.citiesMap = citiesMap;
    }

    public Map<City, Integer> getCitiesArmies() {
        return citiesArmies;
    }

    public void setCitiesArmies(Map<City, Integer> citiesArmies) {
        this.citiesArmies = citiesArmies;
    }

    public Map<City, UserPublicData> getUsersCities() {
        return usersCities;
    }

    public void setUsersCities(Map<City, UserPublicData> usersCities) {
        this.usersCities = usersCities;
    }

    public Map<UserPublicData, Color> getUsersColor() {
        return usersColor;
    }

    public void setUsersColor(Map<UserPublicData, Color> usersColor) {
        this.usersColor = usersColor;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
        return "Game{" +
                "citiesMap=" + citiesMap +
                ", citiesArmies=" + citiesArmies +
                ", usersCities=" + usersCities +
                ", usersColor=" + usersColor +
                ", startTime=" + startTime +
                ", armySpeed=" + armySpeed +
                ", armyGrowthRate=" + armyGrowthRate +
                '}';
    }
}
