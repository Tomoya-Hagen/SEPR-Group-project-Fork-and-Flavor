package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDescriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepRecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface RecipeStepMapper {

    default ArrayList<RecipeStepDetailDto> recipeStepListToRecipeStepDetailDtoList(
        List<RecipeStep> recipeSteps) {
        ArrayList<RecipeStepDetailDto> result = new ArrayList<>();
        for (int i = 0; i < recipeSteps.size(); i++) {
            if (recipeSteps.get(i) instanceof RecipeRecipeStep recipeRecipeStep) {
                result.add(recipeStepToRecipeStepDetailDto(recipeRecipeStep));
            } else {
                result.add(recipeStepToRecipeStepDetailDto((RecipeDescriptionStep) recipeSteps.get(i)));
            }
        }
        return result;
    }

    RecipeStepDescriptionDetailDto recipeStepToRecipeStepDetailDto(RecipeDescriptionStep recipeDescriptionStep);

    @Mapping(source = "recipeRecipe", target = "recipe")
    RecipeStepRecipeDetailDto recipeStepToRecipeStepDetailDto(RecipeRecipeStep recipeRecipeStep);

    default RecipeStepDto recipeDescriptionStepToRecipeStepDto(RecipeDescriptionStep recipeDescriptionStep) {
        return new RecipeStepDto(
            recipeDescriptionStep.getName(),
            recipeDescriptionStep.getDescription(),
            0,
            true
        );
    }

    default RecipeStepDto recipeRecipeStepToRecipeStepDto(RecipeRecipeStep recipeRecipeStep) {
        return new RecipeStepDto(
            recipeRecipeStep.getName(),
            null, // No description for recipe reference step
            recipeRecipeStep.getRecipeRecipe().getId(),
            false // Indicates it's a recipe reference step
        );
    }

    default RecipeStepDto recipeStepToRecipeStepDto(RecipeStep recipeStep) {
        if (recipeStep instanceof RecipeRecipeStep) {
            return recipeRecipeStepToRecipeStepDto((RecipeRecipeStep) recipeStep);
        } else {
            return recipeDescriptionStepToRecipeStepDto((RecipeDescriptionStep) recipeStep);
        }
    }
}
