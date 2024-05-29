package at.ac.tuwien.sepr.groupphase.backend.exception;

public class RecipeStepSelfReferenceException extends RuntimeException {
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
