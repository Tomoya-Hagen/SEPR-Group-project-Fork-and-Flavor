package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;

public interface RecipeService {

    /**
     * Creates a new recipe entry.
     *
     * @param recipe to create
     * @return created recipe entry
     */
    DetailedRecipeDto createRecipe(RecipeCreateDto recipe);
}
