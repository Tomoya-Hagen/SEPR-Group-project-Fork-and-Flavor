package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDescriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepRecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RecipeStepMapper {
    List<RecipeStepDetailDto> recipeStepsListToRecipeStepDetailDtoList(List<RecipeStep> recipeSteps);

    RecipeStepDescriptionDetailDto recipeDescriptionStepToRecipeStepDescriptionDetailDto(
        RecipeDescriptionStep recipeDescriptionStep);

    RecipeStepRecipeDetailDto recipeRecipeStepToRecipeStepRecipeDetailDto(
        RecipeRecipeStep recipeRecipeStep);
}
