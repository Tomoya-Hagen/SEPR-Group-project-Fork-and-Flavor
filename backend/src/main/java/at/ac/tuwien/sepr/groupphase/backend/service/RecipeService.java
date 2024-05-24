package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface representing the recipe service layer.
 */
@Service
public interface RecipeService {
    RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException;

    /**
     * Updates recipe with the given recipeDetailDto.
     *
     * @param recipeDetailDto contains the values to update recipe with
     * @return The updated recipe
     */
    Recipe updateRecipe(RecipeDetailDto recipeDetailDto);

    List<RecipeListDto> getRecipesFromPageInSteps(int pageNumber, int stepNumber);
}
