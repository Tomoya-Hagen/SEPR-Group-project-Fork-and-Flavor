package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AllergenDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NutritionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RecipeMapper {
    RecipeDetailDto recipeToRecipeDetailDto(Recipe recipe, List<IngredientDetailDto> ingredientDetailDtos, List<NutritionDetailDto> nutritionDetailDtos, List<AllergenDetailDto> allergenDetailDtos, List<RecipeStepDetailDto> recipeStepDetailDtos);
}
