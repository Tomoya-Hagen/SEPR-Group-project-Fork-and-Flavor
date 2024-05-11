package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;

import java.util.ArrayList;
import java.util.List;

public record RecipeDetailDto(
    long id,
    String name,
    String description,
    Short numberOfServings,
    long forkedFromId,
    long ownerId,
    ArrayList<CategoryDetailDto> categories,
    boolean isDraft,
    ArrayList<RecipeStepDetailDto> recipeSteps,
    ArrayList<IngredientDetailDto> ingredients,
    ArrayList<AllergenDetailDto> allergens,
    ArrayList<NutritionDetailDto> nutritions
) {
}
