package Protocol.MessageValues;


import Protocol.MessageValue;

import java.util.Map;

public final class User extends MessageValue {

    String string;
    Map<Integer, String> map;


    public User(String string, Map<Integer, String> map) {
        this.string = string;
        this.map = map;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    public void setMap(Map<Integer, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "User{" +
                "string='" + string + '\'' +
                ", map=" + map +
                '}';
    }
}
