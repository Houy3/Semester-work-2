package Protocol.MessageValues.models.CitiesMap;

public class City {

    private int number;
    //от 5 до 95 условных единиц
    private int x;
    //от 5 до 95 условных единиц
    private int y;

    public City(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "City{" +
                "number=" + number +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return number == city.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
