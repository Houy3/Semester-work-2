package Protocol.exceptions;

public class MismatchedClassException extends Exception{

    public MismatchedClassException() {
        super();
    }

    public MismatchedClassException(String message) {
        super(message);
    }

    public MismatchedClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public MismatchedClassException(Throwable cause) {
        super(cause);
    }

    protected MismatchedClassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
