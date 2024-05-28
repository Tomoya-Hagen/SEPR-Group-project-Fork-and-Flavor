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
import java.util.List;

/**
 * This mapper is used to map recipes zu all kinds of different dto types.
 */

@Mapper(uses = {RecipeStepMapper.class, CategoryMapper.class, AllergenMapper.class,
    IngredientMapper.class, NutritionMapper.class})
public interface RecipeMapper {
    /**
     * This method creates a RecipeDetailDto out of the data of the recipe.
     *
     * @param recipe      represents the entity in the data storage.
     * @param ingredients represents the ingredients of the given recipe with the used amount.
     * @param nutritions  represents the nutrition data of the given recipe.
     * @param allergens   represents the allergens that the given recipe contains.
     * @param owner       represents the owner of the given recipe.
     * @param rating      represents the average taste rating of the given recipe.
     * @return a RecipeDetailDto which contains the given parameters.
     */
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

    @Mapping(source = "recipeListDto.id", target = "id")
    default List<Recipe> listOfRecipeListDtoToRecipeList(List<RecipeListDto> recipeListDto) {
        List<Recipe> recipeList = new ArrayList<>();
        for (RecipeListDto recipeListDto1 : recipeListDto) {
            recipeList.add(recipeListDtoToRecipe(recipeListDto1));
        }
        return recipeList;
    }

    /**
     * This method creates a RecipeListDto out of a recipe entity and the average rating based on the taste.
     *
     * @param recipe represents the entity of a recipe.
     * @param rating is the average rating of the given recipe based on the taste.
     * @return a RecipeListDto based on the given parameters.
     */
    @Mapping(source = "recipe.name", target = "name")
    @Mapping(source = "recipe.description", target = "description")
    RecipeListDto recipeAndAverageRatingToRecipeListDto(Recipe recipe, long rating);

    Recipe recipeListDtoToRecipe(RecipeListDto recipeListDto);

    default List<RecipeListDto> recipesToRecipeListDto(List<Recipe> recipes) {
        List<RecipeListDto> recipeList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            recipeList.add(recipeAndAverageRatingToRecipeListDto(recipe, 0));
        }
        return recipeList;
    }

}