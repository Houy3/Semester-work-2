package Server.models;

import Protocol.MessageValues.Game.Game;
import Protocol.MessageValues.Game.GameInitializationForm;
import Protocol.MessageValues.User.User;
import Protocol.MessageValues.models.CitiesMap.CitiesMap;
import Protocol.MessageValues.models.CitiesMap.City;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    private AutoIncrement autoIncrement;
    private boolean isStarted = false;
    public void start() {
        isStarted = true;
        autoIncrement = new AutoIncrement(this);
        autoIncrement.start();
    }

    public void end() {
        isStarted = false;
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

    public void incrementArmies() {
        lock.lock();
        for (City city : citiesArmies.keySet()) {
            citiesArmies.replace(city, citiesArmies.get(city) + armyGrowthRate);
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
