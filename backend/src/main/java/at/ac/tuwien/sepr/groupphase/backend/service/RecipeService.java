package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;

import java.util.stream.Stream;

public interface RecipeService {

    /**
     * Creates a new recipe entry.
     *
     * @param recipe to create
     * @return created recipe entry
     */
    DetailedRecipeDto createRecipe(RecipeCreateDto recipe);

    /**
     * Find all recipes having a name like name.
     *
     * @param name name of recipes to find
     *       limit max amount of recipe to return
     * @return limit amount of recipes found
     */
    Stream<SimpleRecipeResultDto> byname(String name, int limit);
}
