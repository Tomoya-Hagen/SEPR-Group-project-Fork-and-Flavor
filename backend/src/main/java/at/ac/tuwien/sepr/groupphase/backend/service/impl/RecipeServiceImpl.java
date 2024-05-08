package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class RecipeServiceImpl implements RecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe updateRecipe(RecipeDetailDto recipeDetailDto) {
        LOGGER.trace("updateRecipe({})", recipeDetailDto);
        Recipe recipe = recipeRepository.findById(recipeDetailDto.id()).orElseThrow(EntityNotFoundException::new);

        if (recipeDetailDto.name() != null) {
            recipe.setName(recipeDetailDto.name());
        }
        if (recipeDetailDto.description() != null) {
            recipe.setDescription(recipeDetailDto.description());
        }
        if (recipeDetailDto.numberOfServings() != null) {
            recipe.setNumberOfServings(recipeDetailDto.numberOfServings());
        }
        if (recipeDetailDto.categories() != null) {
            recipe.setCategories(recipeDetailDto.categories());
        }
        if (recipeDetailDto.ingredients() != null) {
            recipe.setIngredients(recipeDetailDto.ingredients());
        }
        if (recipeDetailDto.isDraft() != null) {
            recipe.setDraft(recipeDetailDto.isDraft());
        }
        if (recipeDetailDto.forkedFrom() != null) {
            recipe.setForkedFrom(recipeDetailDto.forkedFrom().getId());
        }
        if (!recipeDetailDto.recipeSteps().isEmpty()) {
            recipe.setRecipeSteps(recipeDetailDto.recipeSteps());
        }

        return recipeRepository.save(recipe);
    }

}
