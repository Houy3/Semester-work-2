package Protocol.MessageValues.User;

import Protocol.MessageValues.MessageValue;

public final class UserDataSetForm implements MessageValue {

    private String nickname;

    public UserDataSetForm(String nickname) {
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
        return "UserDataUpdateForm{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
