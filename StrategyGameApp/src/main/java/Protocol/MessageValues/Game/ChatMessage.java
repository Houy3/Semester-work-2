package Protocol.MessageValues.Game;

import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.User;

public final class ChatMessage implements MessageValue {

    private User user;

    private String message;

    public ChatMessage(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
