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
import org.springframework.data.domain.Pageable;
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
     * Forks an existing recipe.
     *
     * @param recipe to create
     * @param forkid id to parent recipe
     * @return created recipe entry
     */
    DetailedRecipeDto forkRecipe(RecipeCreateDto recipe, int forkid) throws ValidationException, RecipeStepNotParsableException, RecipeStepSelfReferenceException;

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
     * finds a recipeDetailDto based on the given recipe id.
     *
     * @param id represents the id of a recipe.
     * @param recursive get ingredients rekursive.
     * @return a RecipeDetailDto if a recipe with the given id exists.
     * @throws NotFoundException if no recipe with the given id was found.
     */
    RecipeDetailDto getRecipeDetailDtoById(long id, boolean recursive) throws NotFoundException;

    /**
     * finds recipes that go well with the given recipe id.
     *
     * @param id represents the id of a recipe.
     * @param pageable the page information.
     * @return a RecipeDetailDto if a recipe with the given id exists.
     * @throws NotFoundException if no recipe with the given id was found.
     */
    Page<RecipeListDto> getRecipesThatGoWellWith(long id, Pageable pageable) throws NotFoundException;

    /**
     * Adds recipes that go well with the given recipe id.
     *
     * @param id represents the id of a recipe.
     * @param goWellWith the recipes that go well with the given recipe.
     * @return a RecipeDetailDto if a recipe with the given id exists.
     * @throws NotFoundException if no recipe with the given id was found.
     */
    RecipeDetailDto addGoesWellWith(long id, List<RecipeListDto> goWellWith) throws NotFoundException;

    /**
     * Updates recipe with the given recipeDetailDto.
     *
     * @param recipeUpdateDto contains the values to update recipe with
     * @return The updated recipe
     */
    DetailedRecipeDto updateRecipe(RecipeUpdateDto recipeUpdateDto);

    /**
     * This method finds a page of recipes by name.
     *
     * @param name the name or parts of it of the recipe to look for.
     * @param pageable the page information.
     * @return A list of RecipeListDto.
     */
    Page<RecipeListDto> getRecipesByName(String name, Pageable pageable);
}
