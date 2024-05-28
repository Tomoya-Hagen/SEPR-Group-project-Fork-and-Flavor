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
     * finds a recipeDetailDto based on the given recipe id.
     *
     * @param id represents the id of a recipe.
     * @return a RecipeDetailDto if a recipe with the given id exists.
     * @throws NotFoundException if no recipe with the given id was found.
     */
    RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException;


    /**
     * finds a list of RecipeListDto based on the given parameters.
     *
     * @param pageNumber represents the page number of recipes.
     * @param stepNumber represents the number of how many recipes are shown per page.
     * @return the recipes of the given page.
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
