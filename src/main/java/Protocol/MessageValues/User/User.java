package Protocol.MessageValues.User;

import Protocol.MessageValues.MessageValue;

public final class User implements MessageValue {

    private String nickname;

    public User(String nickname) {
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
        return "User{" +
                "nickname='" + nickname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return nickname.equals(user.nickname);
    }

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }
}
