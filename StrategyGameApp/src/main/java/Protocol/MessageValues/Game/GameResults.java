package Protocol.MessageValues.Game;

import Protocol.MessageValues.MessageValue;
import Protocol.MessageValues.User.UserPublicData;

public final class GameResults implements MessageValue {

    private UserPublicData winner;

    private Long gameTime; //сколько времени длилась игра

    public GameResults(UserPublicData winner, Long gameTime) {
        this.winner = winner;
        this.gameTime = gameTime;
    }

    public UserPublicData getWinner() {
        return winner;
    }

    public void setWinner(UserPublicData winner) {
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
