package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDescriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepRecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * This mapper creates all types of RecipeStepDto out of a recipeStep entity.
 */
@Mapper
public interface RecipeStepMapper {
    /**
     * This method creates a list of RecipeStepDto out of a list of RecipeStep.
     *
     * @param recipeSteps represents the recipeSteps that should be converted to a list of recipeStepDto.
     * @return the list of recipeStepDto
     */
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

    /**
     * This method converts a recipeDescriptionStep to a RecipeStepDescriptionDetailDto.
     *
     * @param recipeDescriptionStep that should be converted to a dto
     * @return the converted dto based on the given entity
     */
    RecipeStepDescriptionDetailDto recipeStepToRecipeStepDetailDto(RecipeDescriptionStep recipeDescriptionStep);

    /**
     * This method converts a recipeRecipeStep to a RecipeStepRecipeDetailDto.
     *
     * @param recipeRecipeStep that should be converted to a dto
     * @return the converted dto based on the given entity
     */
    @Mapping(source = "recipeRecipe", target = "recipe")
    RecipeStepRecipeDetailDto recipeStepToRecipeStepDetailDto(RecipeRecipeStep recipeRecipeStep);
}
