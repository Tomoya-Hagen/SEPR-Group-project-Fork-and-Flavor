package at.ac.tuwien.sepr.groupphase.backend.exception;

public class UsernameException extends RuntimeException {

    public UsernameException() {
    }

    public UsernameException(String message) {
        super(message);
    }

    public UsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameException(Exception e) {
        super(e);
    }
}
