package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeBookValidator {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void validateCreate(RecipeBookCreateDto recipeBookCreateDto) throws ValidationException {
        LOGGER.trace("validateCreate called");
        List<String> validationErrors = new ArrayList<>();

        for (UserListDto user : recipeBookCreateDto.users()) {
            if (!userRepository.existsById(user.id())) {
                validationErrors.add("User with id: " + user.id() + " does not exist");
            }
        }
        for (RecipeListDto recipeListDto : recipeBookCreateDto.recipes()) {
            if (!recipeRepository.existsById(recipeListDto.id())) {
                validationErrors.add("Recipe with id: " + recipeListDto.id() + " does not exist");
            }
        }

        if (!validationErrors.isEmpty()) {
            LOGGER.warn("Validation of RecipeBook to be created failed for {}", validationErrors);
            throw new ValidationException("Validation of RecipeBook to be created failed", validationErrors);
        }
    }
}
