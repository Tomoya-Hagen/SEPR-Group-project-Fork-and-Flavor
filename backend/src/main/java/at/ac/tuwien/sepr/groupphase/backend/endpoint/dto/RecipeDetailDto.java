package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;


/**
 * Record to hold RecipeDetails.
 *
 * @param id ID of the recipe
 * @param name name of the recipe
 * @param description description of the recipe
 * @param numberOfServings number of servings of the recipe
 * @param categories categories the recipe belongs to
 * @param isDraft boolean that indicates whether this recipe is a draft
 * @param ingredients ingredients of the recipe
 * @param forkedFromId indicates which recipe this one was forked from
 * @param recipeSteps steps that the recipe requires
 * @param forkedRecipes forked recipe that the recipe requires
 * @param ownerId indicates which recipe this one was from owner id
 * @param allergens allergens the recipe belongs to
 * @param nutritions nutritions the recipe belongs to
 * @param rating rating of the recipe
 */
public record RecipeDetailDto(

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
    Long forkedFromId,

    @NotNull
    Long ownerId,

    @NotNull
    ArrayList<CategoryDetailDto> categories,
    @NotNull
    Boolean isDraft,

    @NotNull
    ArrayList<RecipeStepDetailDto> recipeSteps,

    @NotNull
    ArrayList<IngredientDetailDto> ingredients,

    @NotNull
    ArrayList<AllergenDetailDto> allergens,

    @NotNull
    ArrayList<NutritionDetailDto> nutritions,

    @NotNull
    ArrayList<String> forkedRecipes,

    long rating,
    int verifications

) {
}
