package Protocol.MessageValues.Game;

import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.UserPublicData;

public final class GameChatMessage implements MessageValue {

    private UserPublicData user;

    private String message;

    public GameChatMessage(UserPublicData user, String message) {
        this.user = user;
        this.message = message;
    }

    public UserPublicData getUser() {
        return user;
    }

    public void setUser(UserPublicData user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GameChatMessage{" +
                "user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}
