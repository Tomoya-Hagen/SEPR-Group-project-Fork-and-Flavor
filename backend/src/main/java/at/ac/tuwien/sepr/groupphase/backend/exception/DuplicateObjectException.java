package at.ac.tuwien.sepr.groupphase.backend.exception;

/**
 * Exception that signals, that data,
 * that came from outside the backend, is invalid.
 * The data violates some invariant constraint
 * (rather than one, that is imposed by the current data in the system).
 */
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
