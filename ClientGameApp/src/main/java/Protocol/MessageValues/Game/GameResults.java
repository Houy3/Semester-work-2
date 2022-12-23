package Protocol.MessageValues.Game;

import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.User;

public final class GameResults implements MessageValue {

    private User winner;

    private Long gameTime; //сколько времени длилась игра

    public GameResults(User winner, Long gameTime) {
        this.winner = winner;
        this.gameTime = gameTime;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public Long getGameTime() {
        return gameTime;
    }

    public void setGameTime(Long gameTime) {
        this.gameTime = gameTime;
    }

    @Override
    public String toString() {
        return "GameResults{" +
                "winner=" + winner +
                ", seconds=" + gameTime +
                '}';
    }
}
