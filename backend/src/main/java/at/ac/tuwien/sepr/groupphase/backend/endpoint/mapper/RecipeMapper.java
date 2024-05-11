package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

@Mapper(uses = {RecipeStepMapper.class, CategoryMapper.class, AllergenMapper.class,
    IngredientMapper.class, NutritionMapper.class})
public interface RecipeMapper {
    @Mapping(source = "ingredients", target = "ingredients")
    @Mapping(source = "nutritions", target = "nutritions")
    @Mapping(source = "allergens", target = "allergens")
    RecipeDetailDto recipeToRecipeDetailDto(Recipe recipe, HashMap<Ingredient,
        RecipeIngredient> ingredients, HashMap<Nutrition, BigDecimal> nutritions, ArrayList<Allergen> allergens);
}
