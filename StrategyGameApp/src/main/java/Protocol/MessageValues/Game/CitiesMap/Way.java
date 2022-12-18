package Protocol.MessageValues.Game.CitiesMap;

public class Way {

    private City start;
    private City end;

    public Way(City start, City end) {
        this.start = start;
        this.end = end;
    }

    public City getStart() {
        return start;
    }

    public void setStart(City start) {
        this.start = start;
    }

    public City getEnd() {
        return end;
    }

    public void setEnd(City end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Way{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
