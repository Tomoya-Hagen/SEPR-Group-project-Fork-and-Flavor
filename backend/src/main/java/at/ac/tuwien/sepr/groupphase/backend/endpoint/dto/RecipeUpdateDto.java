package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;


/**
 * Record to hold RecipeDetails.
 *
 * @param id ID of the recipe
 * @param name name of the recipe
 * @param description description of the recipe
 * @param numberOfServings number of servings of the recipe
 * @param categories categories the recipe belongs to
 * @param ingredients ingredients of the recipe
 * @param recipeSteps steps that the recipe requires
 */
public record RecipeUpdateDto(

    @NotNull
    Long id,

    @NotNull
    @Size(min = 1, max = 50)
    String name,

    @NotNull
    @Size(min = 1, max = 5000)
    String description,

    @NotNull
    @Min(1)
    Short numberOfServings,

    @NotNull
    List<RecipeCategoryDto> categories,

    @NotNull
    List<RecipeStepDto> recipeSteps,

    @NotNull
    List<RecipeIngredientDto> ingredients
) {
}
