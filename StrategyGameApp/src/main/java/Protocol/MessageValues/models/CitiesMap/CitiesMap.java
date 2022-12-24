package Protocol.MessageValues.models.CitiesMap;

import java.util.Set;

public record CitiesMap(Set<City> cities, Set<Way> ways) {

    @Override
    public String toString() {
        return "CitiesMap{" +
                "cities=" + cities +
                ", ways=" + ways +
                '}';
    }
}
