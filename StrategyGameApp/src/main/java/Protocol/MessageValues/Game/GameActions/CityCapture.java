package Protocol.MessageValues.Game.GameActions;

import Protocol.MessageValues.Game.CitiesMap.City;
import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.UserPublicData;

public final class CityCapture implements MessageValue {

    private City city;

    private UserPublicData user;

    private int armyCount;

    public CityCapture(City city, UserPublicData user, int armyCount) {
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

    public UserPublicData getUser() {
        return user;
    }

    public void setUser(UserPublicData user) {
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
