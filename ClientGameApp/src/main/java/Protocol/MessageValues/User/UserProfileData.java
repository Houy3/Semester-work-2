package Protocol.MessageValues.User;

import Protocol.MessageValues.MessageValue;

public final class UserProfileData implements MessageValue {

    private Integer something;
    //тут будет куча различных параметров

    public UserProfileData(Integer something) {
        this.something = something;
    }

    public Integer getSomething() {
        return something;
    }

    public void setSomething(Integer something) {
        this.something = something;
    }

    @Override
    public String toString() {
        return "UserProfileData{" +
                "something=" + something +
                '}';
    }
}
