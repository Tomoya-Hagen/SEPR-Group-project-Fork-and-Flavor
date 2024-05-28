package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecipeService {
    RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException;

    List<RecipeListDto> getRecipesFromPageInSteps(int pageNumber, int stepNumber);

    /**
     * Search for recipes in the persistent data store matching provided field.
     * The name is considered a match, if the search string is a substring of the field in recipes.
     *
     * @param name The name of the recipe.
     * @return A list of RecipeListDto objects that represent the recipe that match the search criteria.
     * @throws NotFoundException If no recipe are found with the provided name.
     */
    List<RecipeListDto> searchRecipe(String name) throws NotFoundException;

    List<RecipeListDto> getRecipe();

}
