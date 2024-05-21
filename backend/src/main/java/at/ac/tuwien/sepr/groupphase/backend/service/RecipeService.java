package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.List;

@Service
public interface RecipeService {

    /**
     * Creates a new recipe entry.
     *
     * @param recipe to create
     *  usermail for owner
     * @return created recipe entry
     */
    DetailedRecipeDto createRecipe(RecipeCreateDto recipe, String usermail);

    /**
     * Find all recipes having a name like name.
     *
     * @param name name of recipes to find
     *       limit max amount of recipe to return
     * @return limit amount of recipes found
     */
    Stream<SimpleRecipeResultDto> byname(String name, int limit);

    RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException;

    List<RecipeListDto> getRecipesFromPageInSteps(int pageNumber, int stepNumber);

}
