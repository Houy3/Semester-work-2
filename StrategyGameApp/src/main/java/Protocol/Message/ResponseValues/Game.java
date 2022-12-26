package Protocol.Message.ResponseValues;

import Protocol.Message.RequestValues.RequestValue;
import Protocol.Message.models.CitiesMap;
import Protocol.Message.models.City;

import java.util.Date;
import java.util.Map;

public record Game(CitiesMap citiesMap,
                   Date startTime,
                   Map<City, Integer> citiesArmies,
                   Map<City, User> usersCities,
                   int armySpeed,
                   int armyGrowthRate) implements ResponseValue, RequestValue {

    @Override
    public String toString() {
        return "Game{" +
                "citiesMap=" + citiesMap +
                ", citiesArmies=" + citiesArmies +
                ", usersCities=" + usersCities +
                ", startTime=" + startTime +
                ", armySpeed=" + armySpeed +
                ", armyGrowthRate=" + armyGrowthRate +
                '}';
    }
}
