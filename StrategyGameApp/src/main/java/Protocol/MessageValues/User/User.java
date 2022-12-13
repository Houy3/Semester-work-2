package Protocol.MessageValues.User;


import Protocol.MessageValues.MessageValue;

public final class User extends MessageValue {

    private String email;
    private String nick;
    private String pass;

    public User(String email, String nick, String pass) {
        this.email = email;
        this.nick = nick;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nick='" + nick + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
