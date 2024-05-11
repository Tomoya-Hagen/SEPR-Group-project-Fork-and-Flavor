package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import org.mapstruct.Mapper;

@Mapper
public interface IngredientMapper {
    IngredientDetailDto ingredientAndRecipeIngredientToIngredientDetailDto(
        RecipeIngredient recipeIngredient, Ingredient ingredient);
}
