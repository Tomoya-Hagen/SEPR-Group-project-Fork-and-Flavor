package at.ac.tuwien.sepr.groupphase.backend.exception;

public class DuplicateObjectException extends RuntimeException {
    public DuplicateObjectException() {
    }

    public DuplicateObjectException(String message) {
        super(message);
    }

    public DuplicateObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateObjectException(Exception e) {
        super(e);
    }
}
