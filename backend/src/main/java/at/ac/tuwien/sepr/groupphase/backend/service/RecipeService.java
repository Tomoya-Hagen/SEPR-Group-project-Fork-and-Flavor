package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is the interface for the service layer of Recipes.
 *
 */
@Service
public interface RecipeService {
    /**
     * This function finds the recipe with the given ID.
     *
     * @param id ID of the recipe to find.
     * @return The detailed dto of the recipe.
     * @throws NotFoundException Thrown when no recipe with the given ID exists.
     */
    RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException;

    /**
     * Returns a list of recipes found using the page number and step number.
     *
     * @param pageNumber the number of pages that the recipe has.
     * @param stepNumber the number of steps that the recipe has.
     * @return a list of RecipeListDto
     */
    List<RecipeListDto> getRecipesFromPageInSteps(int pageNumber, int stepNumber);

    /**
     * This method finds a limited number of recipes with the name, or letters typed in by the user.
     *
     * @param name the name or parts of it of the recipe to look for.
     * @param limit limit to specify the number of recipes to be found.
     * @return A list of RecipeListDto.
     */
    List<RecipeListDto> getRecipesByNames(String name, int limit);
}
