package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

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
     * @return created recipe entry
     */
    DetailedRecipeDto createRecipe(RecipeCreateDto recipe) throws ValidationException, RecipeStepNotParsableException, RecipeStepSelfReferenceException;

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
     * Updates recipe with the given recipeDetailDto.
     *
     * @param recipeUpdateDto contains the values to update recipe with
     * @return The updated recipe
     */
    DetailedRecipeDto updateRecipe(RecipeUpdateDto recipeUpdateDto);

    /**
     * This method finds a limited number of recipes with the name, or letters typed in by the user.
     *
     * @param name the name or parts of it of the recipe to look for.
     * @param limit limit to specify the number of recipes to be found.
     * @return A list of RecipeListDto.
     */
    List<RecipeListDto> getRecipesByNames(String name, int limit);

    /**
     * This method finds a page of recipes by name.
     *
     * @param name the name or parts of it of the recipe to look for.
     * @param page the page number to look for.
     * @param size the size of the page.
     * @return A list of RecipeListDto.
     */
    Page<RecipeListDto> getRecipesByName(String name, int page, int size);
}
