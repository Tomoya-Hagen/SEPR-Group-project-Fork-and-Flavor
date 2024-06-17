package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeBookValidator {

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public RecipeBookValidator(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public void validateForCreateAndUpdate(RecipeBookCreateDto recipeBookCreateDto) throws ValidationException {
        LOGGER.trace("validateForCreateAndUpdate({})", recipeBookCreateDto);
        List<String> validationErrors = new ArrayList<>();

        if (recipeBookCreateDto.name().isEmpty()) {
            validationErrors.add("Name cannot be Empty!");
        }

        if (recipeBookCreateDto.description().isEmpty()) {
            validationErrors.add("Description cannot be empty");
        }

        if (recipeBookCreateDto.users() != null) {
            for (UserListDto user : recipeBookCreateDto.users()) {
                if (!userRepository.existsById(user.id())) {
                    validationErrors.add("User with id: " + user.id() + " does not exist");
                }
            }
        } else {
            validationErrors.add("Users cannot be null");
        }
        if (recipeBookCreateDto.recipes() != null) {
            for (RecipeListDto recipeListDto : recipeBookCreateDto.recipes()) {
                if (!recipeRepository.existsById(recipeListDto.id())) {
                    validationErrors.add("Recipe with id: " + recipeListDto.id() + " does not exist");
                }
            }
        } else {
            validationErrors.add("Recipes cannot be null");
        }

        if (!validationErrors.isEmpty()) {
            LOGGER.warn("Validation of RecipeBook to be created or updated failed for {}", validationErrors);
            throw new ValidationException("Validation of RecipeBook to be created or updated failed", validationErrors);
        }
    }
}
