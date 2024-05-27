package at.ac.tuwien.sepr.groupphase.backend.exception;

public class PasswordException extends RuntimeException {

    public PasswordException() {
    }

    public PasswordException(String message) {
        super(message);
    }

    public PasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordException(Exception e) {
        super(e);
    }
}
