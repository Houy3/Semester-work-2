package Protocol.MessageValues.models.CitiesMap;

import com.example.clientgameapp.models.Route;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public record CitiesMap(Set<City> cities, List<Route> routes)  implements Serializable {

    @Override
    public String toString() {
        return "CitiesGameMap{" +
                "cities=" + cities +
                ", ways=" + routes +
                '}';
    }
}
