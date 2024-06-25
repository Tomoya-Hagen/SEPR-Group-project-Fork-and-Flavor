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
        for (RecipeStep recipeStep : recipeSteps) {
            if (recipeStep instanceof RecipeRecipeStep recipeRecipeStep) {
                result.add(recipeStepToRecipeStepDetailDto(recipeRecipeStep));
            } else {
                result.add(recipeStepToRecipeStepDetailDto((RecipeDescriptionStep) recipeStep));
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

    /**
     * Converts a RecipeDescriptionStep entity to a RecipeStepDto object.
     *
     * @param recipeDescriptionStep The RecipeDescriptionStep entity.
     * @return A RecipeStepDto object with the name and description fields set to the values
     *     from the RecipeDescriptionStep entity. The id field is set to 0 and the
     *     isDescriptionStep field is set to true, indicating it's a description step.
     */
    default RecipeStepDto recipeDescriptionStepToRecipeStepDto(RecipeDescriptionStep recipeDescriptionStep) {
        return new RecipeStepDto(
            recipeDescriptionStep.getName(),
            recipeDescriptionStep.getDescription(),
            0,
            true
        );
    }

    /**
     * Converts a RecipeRecipeStep entity to a RecipeStepDto object.
     *
     * @param recipeRecipeStep The RecipeRecipeStep entity.
     * @return A RecipeStepDto object with the name and ID fields set to the values from the RecipeRecipeStep entity.
     *      The description field is set to null and the isDescriptionStep field is set to false, indicating it's a recipe reference step.
     */
    default RecipeStepDto recipeRecipeStepToRecipeStepDto(RecipeRecipeStep recipeRecipeStep) {
        return new RecipeStepDto(
            recipeRecipeStep.getName(),
            null, // No description for recipe reference step
            recipeRecipeStep.getRecipeRecipe().getId(),
            false // Indicates it's a recipe reference step
        );
    }

    /**
     * Converts a RecipeStep entity to a RecipeStepDto object.
     *
     * @param recipeStep The RecipeStep entity.
     * @return A RecipeStepDto object. If the RecipeStep is an instance of RecipeRecipeStep, it uses the recipeRecipeStepToRecipeStepDto method for conversion.
     *      Otherwise, it uses the recipeDescriptionStepToRecipeStepDto method for conversion.
     */
    default RecipeStepDto recipeStepToRecipeStepDto(RecipeStep recipeStep) {
        if (recipeStep instanceof RecipeRecipeStep) {
            return recipeRecipeStepToRecipeStepDto((RecipeRecipeStep) recipeStep);
        } else {
            return recipeDescriptionStepToRecipeStepDto((RecipeDescriptionStep) recipeStep);
        }
    }

    /**
     * Converts an Object to a Long.
     *
     * @param value The Object to be converted.
     * @return A Long value. If the Object is null, it returns null. If the Object is an instance of Number, it returns the long value of the Number.
     *      If the Object is not an instance of Number, it tries to parse the Object's string representation to a Long. If the parsing fails, it returns null.
     */
    default Long mapToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
