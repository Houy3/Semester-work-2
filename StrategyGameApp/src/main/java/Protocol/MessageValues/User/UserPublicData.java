package Protocol.MessageValues.User;

import Protocol.MessageValues.MessageValue;

public final class UserPublicData implements MessageValue {

    private String nickname;

    public UserPublicData(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "UserPublicData{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
