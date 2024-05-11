package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;

import java.util.List;

public record RecipeDetailDto(
    long id,
    String name,
    String description,
    Short numberOfServings,
    long forkedFromId,
    long ownerId,
    List<CategoryDetailDto> categories,
    boolean isDraft,
    List<RecipeStepDetailDto> recipeSteps,
    List<IngredientDetailDto> ingredients,
    List<AllergenDetailDto> allergens,
    List<NutritionDetailDto> nutritions
) {
}
