package Protocol.MessageValues.Game.GameActions;

import Protocol.MessageValues.User.User;
import Protocol.MessageValues.models.CitiesMap.City;
import Protocol.MessageValues.MessageValue;

public final class CityCapture implements MessageValue {

    private City city;

    private User user;

    private int armyCount;

    public CityCapture(City city, User user, int armyCount) {
        this.city = city;
        this.user = user;
        this.armyCount = armyCount;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getArmyCount() {
        return armyCount;
    }

    public void setArmyCount(int armyCount) {
        this.armyCount = armyCount;
    }

    @Override
    public String toString() {
        return "CityCapture{" +
                "city=" + city +
                ", user=" + user +
                ", armyCount=" + armyCount +
                '}';
    }
}
