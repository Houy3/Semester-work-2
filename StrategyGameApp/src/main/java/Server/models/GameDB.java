package Server.models;

import Protocol.Message.ResponseValues.Game;
import Protocol.Message.RequestValues.GameActionArmyMovement;
import Protocol.Message.RequestValues.GameActionCityCapture;
import Protocol.Message.RequestValues.GameInitializationForm;
import Protocol.Message.ResponseValues.User;
import Protocol.Message.models.CitiesMap;
import Protocol.Message.models.City;
import Server.app.UserConnectionThread;
import Server.models.validators.ValidatorException;
import Server.services.Impl.MapCitiesGenerator;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameDB {

    private CitiesMap citiesMap;

    private Date startTime;


    private Map<City, Integer> citiesArmies;
    private Map<City, UserDB> usersCities;

    //Скорость армии, когда она идет от одного города к другому. усл.ед./с.
    private final int armySpeed;

    //Скорость роста армии игрока в городе ед./с.
    private final int armyGrowthRate;

    private static final int waitingTimeS = 15;

    public GameDB(GameInitializationForm form, List<UserDB> users) {
        MapCitiesGenerator.generateCitiesMap(this, form.countOfCities(), users);
        this.startTime = new Date((new Date()).getTime() + waitingTimeS*1000);
        this.armySpeed = form.armySpeed();
        this.armyGrowthRate = form.armyGrowthRate();
    }

    public Game toGame() {
        Map<City, User> usersCities = new HashMap<>();
        for (City city : this.usersCities.keySet()) {
            usersCities.put(city, this.usersCities.get(city).toUser());
        }

        return new Game(
                citiesMap,
                startTime,
                citiesArmies,
                usersCities,
                armySpeed,
                armyGrowthRate
        );
    }


    private boolean isGameInProcess = false;
    public void start() {
        try {
            Thread.sleep(startTime.getTime() - (new Date()).getTime() );
        } catch (InterruptedException ignored) {}

        isGameInProcess = true;
        ArmyAutoIncrement armyAutoIncrement = new ArmyAutoIncrement(this);
        armyAutoIncrement.start();
    }


    public CitiesMap getCitiesMap() {
        return citiesMap;
    }

    public void setCitiesMap(CitiesMap citiesMap) {
        this.citiesMap = citiesMap;
    }

    public void setCitiesArmies(Map<City, Integer> citiesArmies) {
        this.citiesArmies = citiesArmies;
    }

    public void setUsersCities(Map<City, UserDB> usersCities) {
        this.usersCities = usersCities;
    }


    private final Lock lock = new ReentrantLock();

    public void disconnectUser(UserDB user) {
        lock.lock();
        for (City city : usersCities.keySet()) {
            if (usersCities.get(city).equals(user)) {
                usersCities.remove(city);
            }
        }

        lock.unlock();
    }

    public void moveArmy(GameActionArmyMovement gameActionArmyMovement, UserDB user, UserConnectionThread userConnectionThread) throws ValidatorException {
        lock.lock();
        City startCity = gameActionArmyMovement.way().getStart();
        if (!usersCities.get(startCity).equals(user)) {
            throw new ValidatorException("It's not your city. ");
        }
        if (citiesArmies.get(startCity) < gameActionArmyMovement.armyCount()) {
            throw new ValidatorException("Not enough army. ");
        }

        citiesArmies.replace(startCity, citiesArmies.get(startCity) - gameActionArmyMovement.armyCount());
        lock.unlock();
        new ArmyMove(this, gameActionArmyMovement, user, userConnectionThread, lock).start();
    }




    public void incrementArmies() {
        lock.lock();
        for (City city : citiesArmies.keySet()) {
            if (usersCities.get(city) != null) {
                citiesArmies.replace(city, citiesArmies.get(city) + armyGrowthRate);
            }
        }
        lock.unlock();
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


    private class ArmyMove extends Thread {

        private final GameDB game;
        private final GameActionArmyMovement gameActionArmyMovement;

        private final UserDB user;
        private final UserConnectionThread userConnectionThread;

        public ArmyMove(GameDB game, GameActionArmyMovement gameActionArmyMovement, UserDB user, UserConnectionThread userConnectionThread, Lock lock) {
            this.game = game;
            this.gameActionArmyMovement = gameActionArmyMovement;
            this.user = user;
            this.userConnectionThread = userConnectionThread;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000L * ((long) gameActionArmyMovement.way().getLength()) / armySpeed);
            } catch (InterruptedException ignored) {}
            City endCity = gameActionArmyMovement.way().getEnd();
            lock.lock();
            if (usersCities.get(endCity) == null || usersCities.get(endCity).equals(user)) {
                citiesArmies.replace(endCity, citiesArmies.get(endCity) + gameActionArmyMovement.armyCount());
                userConnectionThread.captureCityWithResponse(new GameActionCityCapture(endCity, user.toUser(), citiesArmies.get(endCity) ));
            } else  {
                citiesArmies.replace(endCity, citiesArmies.get(endCity) - gameActionArmyMovement.armyCount());
                if (citiesArmies.get(endCity) < 0) {
                    citiesArmies.replace(endCity, Math.abs(citiesArmies.get(endCity)) );
                    usersCities.replace(endCity, user);
                    userConnectionThread.captureCityWithResponse(new GameActionCityCapture(endCity, user.toUser(), citiesArmies.get(endCity) ));
                } else {
                    userConnectionThread.captureCityWithResponse(new GameActionCityCapture(endCity, usersCities.get(endCity).toUser(), citiesArmies.get(endCity) ));
                }
            }
            lock.unlock();
        }
    }

    private class ArmyAutoIncrement extends Thread {

        private final GameDB game;

        public ArmyAutoIncrement(GameDB game) {
            this.game = game;
        }

        @Override
        public void run() {
            while (isGameInProcess) {
                game.incrementArmies();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
