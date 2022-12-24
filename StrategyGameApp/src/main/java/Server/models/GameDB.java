package Server.models;

import Protocol.MessageValues.Game.Game;
import Protocol.MessageValues.Game.GameActions.ArmyMovement;
import Protocol.MessageValues.Game.GameActions.CityCapture;
import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.User.User;
import Protocol.MessageValues.models.CitiesMap.CitiesMap;
import Protocol.MessageValues.models.CitiesMap.City;
import Server.app.UserConnectionThread;
import Server.models.validators.ValidatorException;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class GameDB {

    private CitiesMap citiesMap;

    private Date startTime;


    private Map<City, Integer> citiesArmies;
    private Map<City, UserDB> usersCities;

    //Скорость армии, когда она идет от одного города к другому. усл.ед./с.
    private final int armySpeed;

    //Скорость роста армии игрока в городе ед./с.
    private final int armyGrowthRate;


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

    public boolean isEnded()  {
        return new HashSet<>(usersCities.values()).size() < 2;
    }

    private boolean isStarted = false;
    public void start() {
        isStarted = true;
        AutoIncrement autoIncrement = new AutoIncrement(this);
        autoIncrement.start();
    }

    public UserDB end() {
        isStarted = false;
        try {
            return new ArrayList<>(usersCities.values()).get(0);
        } catch (Exception e) {
            return null;
        }
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

    public int getArmyGrowthRate() {
        return armyGrowthRate;
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

    public void moveArmy(ArmyMovement armyMovement, UserDB user, UserConnectionThread userConnectionThread) throws ValidatorException {
        lock.lock();
        City startCity = armyMovement.getWay().getStart();
        if (!usersCities.get(startCity).equals(user)) {
            throw new ValidatorException("It's not your city. ");
        }
        if (citiesArmies.get(startCity) < armyMovement.getArmyCount()) {
            throw new ValidatorException("Not enough army. ");
        }

        citiesArmies.replace(startCity, citiesArmies.get(startCity) - armyMovement.getArmyCount());
        lock.unlock();
        new ArmyMove(this, armyMovement, user, userConnectionThread, lock).start();
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
        private final ArmyMovement armyMovement;

        private final UserDB user;
        private final UserConnectionThread userConnectionThread;

        public ArmyMove(GameDB game, ArmyMovement armyMovement, UserDB user, UserConnectionThread userConnectionThread, Lock lock) {
            this.game = game;
            this.armyMovement = armyMovement;
            this.user = user;
            this.userConnectionThread = userConnectionThread;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000L * ((long)armyMovement.getWay().getLength()) / game.getArmySpeed());
            } catch (InterruptedException ignored) {}
            City endCity = armyMovement.getWay().getEnd();
            lock.lock();
            if (game.getUsersCities().get(endCity) == null || game.getUsersCities().get(endCity).equals(user)) {
                citiesArmies.replace(endCity, citiesArmies.get(endCity) + armyMovement.getArmyCount());
                userConnectionThread.captureCityWithResponse(new CityCapture(endCity, user.toUser(), citiesArmies.get(endCity) ));
            } else  {
                citiesArmies.replace(endCity, citiesArmies.get(endCity) - armyMovement.getArmyCount());
                if (citiesArmies.get(endCity) < 0) {
                    citiesArmies.replace(endCity, Math.abs(citiesArmies.get(endCity)) );
                    usersCities.replace(endCity, user);
                    userConnectionThread.captureCityWithResponse(new CityCapture(endCity, user.toUser(), citiesArmies.get(endCity) ));
                } else {
                    userConnectionThread.captureCityWithResponse(new CityCapture(endCity, usersCities.get(endCity).toUser(), citiesArmies.get(endCity) ));
                }
            }
            lock.unlock();
        }
    }

    private class AutoIncrement extends Thread {

        private final GameDB game;

        public AutoIncrement(GameDB game) {
            this.game = game;
        }

        @Override
        public void run() {
            while (isStarted) {
                if ((new Date()).getTime() < startTime.getTime()) {
                    game.incrementArmies();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
