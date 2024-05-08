package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.springframework.stereotype.Service;

/**
 * Interface representing the recipe service layer
 */
@Service
public interface RecipeService {

    /**
     * Updates recipe with the given recipeDetailDto
     *
     * @param recipeDetailDto contains the values to update recipe with
     * @return The updated recipe
     */
    Recipe updateRecipe(RecipeDetailDto recipeDetailDto);
}
