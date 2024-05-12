package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
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
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "recipe.id", target = "id")
    @Mapping(source = "recipe.isDraft", target = "isDraft")
    @Mapping(source = "recipe.forkedFrom.id", target = "forkedFromId")
    RecipeDetailDto recipeToRecipeDetailDto(Recipe recipe, HashMap<Ingredient,
        RecipeIngredient> ingredients, HashMap<Nutrition, BigDecimal> nutritions, ArrayList<Allergen> allergens, ApplicationUser owner, long rating);

    default ArrayList<RecipeListDto> recipeListAndRatingListToListOfRecipeRatingDto(ArrayList<Recipe> recipes, ArrayList<Long> ratings) {
        ArrayList<RecipeListDto> recipeListDtos = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            recipeListDtos.add(recipeAndAverageRatingToRecipeListDto(recipes.get(i), ratings.get(i)));
        }
        return recipeListDtos;
    }

    RecipeListDto recipeAndAverageRatingToRecipeListDto(Recipe recipe, long rating);
}
