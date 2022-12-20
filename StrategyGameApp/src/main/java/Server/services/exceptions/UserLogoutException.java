package Server.services.exceptions;

public class UserLogoutException extends Exception {
    public UserLogoutException() {
        super();
    }

    public UserLogoutException(String message) {
        super(message);
    }

    public UserLogoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserLogoutException(Throwable cause) {
        super(cause);
    }

    protected UserLogoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
