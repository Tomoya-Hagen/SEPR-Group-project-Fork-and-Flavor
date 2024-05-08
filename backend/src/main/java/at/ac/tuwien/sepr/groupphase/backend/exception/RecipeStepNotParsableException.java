package at.ac.tuwien.sepr.groupphase.backend.exception;

public class RecipeStepNotParsableException extends RuntimeException{
    public RecipeStepNotParsableException() {
    }

    public RecipeStepNotParsableException(String message) {
        super(message);
    }

    public RecipeStepNotParsableException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipeStepNotParsableException(Throwable cause) {
        super(cause);
    }
}
