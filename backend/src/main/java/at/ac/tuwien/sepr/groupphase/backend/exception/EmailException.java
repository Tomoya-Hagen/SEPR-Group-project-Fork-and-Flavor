package at.ac.tuwien.sepr.groupphase.backend.exception;

public class EmailException extends RuntimeException {

    public EmailException() {
    }

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailException(Exception e) {
        super(e);
    }
}
