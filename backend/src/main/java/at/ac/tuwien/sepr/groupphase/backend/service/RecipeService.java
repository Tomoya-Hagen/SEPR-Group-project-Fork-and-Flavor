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
    RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException;

    List<RecipeListDto> getRecipesFromPageInSteps(int pageNumber, int stepNumber);

    List<RecipeListDto> getRecipesByNames(String name, int limit);
}
