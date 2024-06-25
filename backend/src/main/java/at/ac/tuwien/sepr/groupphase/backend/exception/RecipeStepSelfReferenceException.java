package at.ac.tuwien.sepr.groupphase.backend.exception;

/**
 * Exception that signals, that data,
 * that came from outside the backend, is invalid.
 * The data violates some invariant constraint
 * (rather than one, that is imposed by the current data in the system).
 */
public class RecipeStepSelfReferenceException  extends RuntimeException {
    public RecipeStepSelfReferenceException() {
    }

    public RecipeStepSelfReferenceException(String message) {
        super(message);
    }

    public RecipeStepSelfReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipeStepSelfReferenceException(Exception e) {
        super(e);
    }
}