package TestClient;

import Protocol.Message.models.City;
import Protocol.Message.models.Way;

import java.util.HashSet;
import java.util.Set;

public class Test2 {
    public static void main(String[] args) {

        Set<Way> set = new HashSet<>();
        Way way1 = new Way(
                new City(6,1,1),
                new City(1,1, 1)
        );
        set.add(way1);

        Way way2 = new Way(
                new City(1,10,10),
                new City(6,10, 10)
        );

        System.out.println(set.contains(way2));
    }
}
