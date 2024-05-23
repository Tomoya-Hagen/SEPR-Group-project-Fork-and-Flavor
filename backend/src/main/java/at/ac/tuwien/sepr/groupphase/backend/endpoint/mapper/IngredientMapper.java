package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This mapper is used to map an ingredient entity to a dto and reverse.
 */

@Mapper
public interface IngredientMapper {
    /**
     * This method creates a list of IngredientDetailDto from a hashmap.
     *
     * @param ingredientRecipeIngredientHashMap the hashmap, that contains information about the ingredients themselves
     *                                          and the amount that is used of this ingredient in a recipe.
     * @return a list of IngredientDetailDto which contains information about the ingredients in a recipe.
     */
    default ArrayList<IngredientDetailDto> ingredientAndRecipeIngredientHashmapToIngredientDetailDtoList(HashMap<Ingredient, RecipeIngredient> ingredientRecipeIngredientHashMap) {
        ArrayList<IngredientDetailDto> result = new ArrayList<>();
        for (Ingredient ingredient : ingredientRecipeIngredientHashMap.keySet()) {
            result.add(ingredientAndRecipeIngredientToIngredientDetailDto(ingredient,
                ingredientRecipeIngredientHashMap.get(ingredient)));
        }
        return result;
    }


    /**
     * This method creates a IngredientDetailDto an ingredient entity and a recipeIngredient entity.
     *
     * @param ingredient represents an ingredient of a recipe.
     * @param recipeIngredient contains the unit and the amount that is used in a recipe.
     * @return a IngredientDetailDto which contains the combined data of the given entities.
     */
    @Mapping(source = "ingredient.id", target = "id")
    @Mapping(source = "ingredient.name", target = "name")
    @Mapping(source = "recipeIngredient.unit", target = "unit")
    @Mapping(source = "recipeIngredient.amount", target = "amount")
    IngredientDetailDto ingredientAndRecipeIngredientToIngredientDetailDto(
        Ingredient ingredient, RecipeIngredient recipeIngredient);

}
