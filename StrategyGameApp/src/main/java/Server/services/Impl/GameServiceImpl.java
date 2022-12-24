package Server.services.Impl;

import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.models.CitiesMap.CitiesMap;
import Protocol.MessageValues.models.CitiesMap.City;
import Protocol.MessageValues.models.CitiesMap.Way;
import Server.models.GameDB;
import Server.models.RoomDB;
import Server.models.UserDB;
import Server.services.Inter.GameService;

import java.util.*;

public class GameServiceImpl implements GameService {

    private static final int secondsToStart = 10;

    private static final int startArmy = 10;



    @Override
    public GameDB initialize(GameInitializationForm form, RoomDB room) {
        GameDB game = new GameDB(form);

        game.setCitiesMap(generateCitiesMap(form.getCountOfCities()));
        game.setCitiesArmies(generateCitiesArmies(game.getCitiesMap()));
        game.setUsersCities(generateUsersCities(game.getCitiesMap(), room.getUsers()));

        game.setStartTime(new Date((new Date()).getTime() + secondsToStart * 1000));

        return game;
    }

    private Map<City, Integer> generateCitiesArmies(CitiesMap citiesMap) {
        Map<City, Integer> citiesArmies = new HashMap<>();

        for (City city : citiesMap.cities()) {
            citiesArmies.put(city, startArmy);
        }

        return citiesArmies;
    }

    private Map<City, UserDB> generateUsersCities(CitiesMap citiesMap, List<UserDB> users) {
        Map<City, UserDB> usersCities = new HashMap<>();

        for (City city : citiesMap.cities()) {
            if (city.number() == 1) {
                usersCities.put(city, users.get(0));
            }
            if (city.number() == 4) {
                usersCities.put(city, users.get(1));
            }
        }

        return usersCities;
    }

    private CitiesMap generateCitiesMap(int countOfCities) {
        City city1 = new City(1, 25, 2);
        City city2 = new City(2, 5, 42);
        City city3 = new City(3, 25, 82);
        City city4 = new City(4, 65, 82);
        City city5 = new City(5, 85, 42);
        City city6 = new City(6, 65, 2);

        Set<City> cities = new HashSet<>();
        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        cities.add(city4);
        cities.add(city5);
        cities.add(city6);

        Set<Way> ways = new HashSet<>();
        ways.add(new Way(city1, city2));
        ways.add(new Way(city2, city3));
        ways.add(new Way(city3, city4));
        ways.add(new Way(city4, city5));
        ways.add(new Way(city5, city6));
        ways.add(new Way(city6, city1));
        ways.add(new Way(city1, city3));
        ways.add(new Way(city3, city6));
        ways.add(new Way(city4, city6));

        return new CitiesMap(cities, ways);
    }
}
