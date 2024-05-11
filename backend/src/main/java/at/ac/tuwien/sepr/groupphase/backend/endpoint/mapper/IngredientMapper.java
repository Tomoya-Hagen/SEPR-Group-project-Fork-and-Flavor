package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface IngredientMapper {

    default ArrayList<IngredientDetailDto> IngredientAndRecipeIngredientHashmapToIngredientDetailDtoList(HashMap<Ingredient, RecipeIngredient> ingredientRecipeIngredientHashMap) {
        ArrayList<IngredientDetailDto> result = new ArrayList<>();
        for (Ingredient ingredient : ingredientRecipeIngredientHashMap.keySet()) {
            result.add(ingredientAndRecipeIngredientToIngredientDetailDto(ingredient,
                ingredientRecipeIngredientHashMap.get(ingredient)));
        }
        return result;
    }

    @Mapping(source = "ingredient.id", target = "id")
    @Mapping(source = "ingredient.name", target = "name")
    @Mapping(source = "recipeIngredient.unit", target = "unit")
    @Mapping(source = "recipeIngredient.amount", target = "amount")
    IngredientDetailDto ingredientAndRecipeIngredientToIngredientDetailDto(
        Ingredient ingredient, RecipeIngredient recipeIngredient);

}
