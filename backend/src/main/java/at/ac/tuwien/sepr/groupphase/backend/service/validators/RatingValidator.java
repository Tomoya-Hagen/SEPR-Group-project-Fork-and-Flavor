package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class RatingValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void validateForCreate(RatingCreateDto ratingCreateDto) throws ValidationException {
        LOGGER.trace("validateForCreate({})", ratingCreateDto);
        List<String> validationErrors = new ArrayList<>();

        if (ratingCreateDto.cost() > 5 || ratingCreateDto.cost() < 1) {
            validationErrors.add("The rating of cost can only be a value from 0 to 5");
        }

        if (ratingCreateDto.easeOfPrep() > 5 || ratingCreateDto.easeOfPrep() < 1) {
            validationErrors.add("The rating of ease of preparation can only be a value from 0 to 5");
        }
        if (ratingCreateDto.taste() > 5 || ratingCreateDto.taste() < 1) {
            validationErrors.add("The rating of taste can only be a value from 0 to 5");
        }

        if (ratingCreateDto.review().isEmpty() || ratingCreateDto.review().length() > 500) {
            validationErrors.add("The review must be at least one character and can be up to 500 characters long.");
        }

        if (!validationErrors.isEmpty()) {
            LOGGER.warn("Validation of RecipeBook to be created failed for {}", validationErrors);
            throw new ValidationException("Validation of RecipeBook to be created failed", validationErrors);
        }
    }
}
