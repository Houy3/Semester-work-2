package Protocol.MessageValues.User;

import Protocol.MessageValues.MessageValue;

public final class UserData implements MessageValue {

    private String nickname;

    private Integer something;
    //тут будет куча различных параметров


    public UserData(String nickname, Integer something) {
        this.nickname = nickname;
        this.something = something;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSomething() {
        return something;
    }

    public void setSomething(Integer something) {
        this.something = something;
    }
}
