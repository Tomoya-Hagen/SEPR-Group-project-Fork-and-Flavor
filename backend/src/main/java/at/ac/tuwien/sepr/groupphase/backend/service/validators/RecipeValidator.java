package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeValidator {

    final CategoryRepository categoryRepository;
    final IngredientRepository ingredientRepository;
    final RecipeRepository recipeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public RecipeValidator(CategoryRepository categoryRepository, IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    public void validateCreate(RecipeCreateDto recipe) throws ValidationException {
        List<String> validationErrors = new ArrayList<>();

        for (RecipeCategoryDto category : recipe.getCategories()) {
            if (!categoryRepository.existsById(category.getId())) {
                validationErrors.add("Category " + category.getId() + " not found");
            }
        }
        for (RecipeIngredientDto ingredient : recipe.getIngredients()) {
            if (!ingredientRepository.existsById(ingredient.getId())) {
                validationErrors.add("Ingredient " + ingredient.getId() + " not found");
            }
        }
        for (RecipeStepDto steps : recipe.getSteps()) {
            if (steps.isCorrect()) {
                if (!steps.isWhichstep() && !recipeRepository.existsById(steps.getRecipeId())) {
                    validationErrors.add("Step " + steps.getName() + " not found");
                }
            } else {
                validationErrors.add("Step " + steps.getName() + " is not valid");
            }


        }

        if (!validationErrors.isEmpty()) {
            LOGGER.warn("Validation of Recipe to create failed for {}", validationErrors);
            throw new ValidationException("Validation of Recipe to create failed", validationErrors);
        }

    }
}
