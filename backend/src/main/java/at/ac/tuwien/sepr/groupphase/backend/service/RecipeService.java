package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.List;

/**
 * This is the interface for the service layer of Recipes.
 *
 */
@Service
public interface RecipeService {


    /**
     * Creates a new recipe entry.
     *
     * @param recipe to create
     *               usermail for owner
     * @return created recipe entry
     */
    DetailedRecipeDto createRecipe(RecipeCreateDto recipe, String usermail) throws ValidationException, RecipeStepNotParsableException, RecipeStepSelfReferenceException;

    /**
     * Find all recipes having a name like name.
     *
     * @param name name of recipes to find
     *             limit max amount of recipe to return
     * @return limit amount of recipes found
     */
    Stream<SimpleRecipeResultDto> byname(String name, int limit);

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
     * Search for recipes in the persistent data store matching provided field.
     * The name is considered a match, if the search string is a substring of the field in recipes.
     *
     * @param name The name of the recipe.
     * @return A list of RecipeListDto objects that represent the recipe that match the search criteria.
     * @throws NotFoundException If no recipe are found with the provided name.
     */
    List<RecipeListDto> searchRecipe(String name) throws NotFoundException;

    /**
     * This method finds a limited number of recipes with the name, or letters typed in by the user.
     *
     * @param name the name or parts of it of the recipe to look for.
     * @param limit limit to specify the number of recipes to be found.
     * @return A list of RecipeListDto.
     */
    List<RecipeListDto> getRecipesByNames(String name, int limit);

}
