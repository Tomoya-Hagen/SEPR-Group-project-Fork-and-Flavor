package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
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
     * @param recipeUpdateDto contains the values to update recipe with
     * @return The updated recipe
     */
    DetailedRecipeDto updateRecipe(RecipeUpdateDto recipeUpdateDto);

    List<RecipeListDto> getRecipesFromPageInSteps(int pageNumber, int stepNumber);
}
