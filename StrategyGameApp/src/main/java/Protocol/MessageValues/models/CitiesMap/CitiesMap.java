package Protocol.MessageValues.models.CitiesMap;

import java.util.List;

public class CitiesMap {

    private List<City> cities;
    private List<Way> ways;

    public CitiesMap(List<City> cities, List<Way> ways) {
        this.cities = cities;
        this.ways = ways;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Way> getWays() {
        return ways;
    }

    public void setWays(List<Way> ways) {
        this.ways = ways;
    }

    @Override
    public String toString() {
        return "CitiesMap{" +
                "cities=" + cities +
                ", ways=" + ways +
                '}';
    }
}
