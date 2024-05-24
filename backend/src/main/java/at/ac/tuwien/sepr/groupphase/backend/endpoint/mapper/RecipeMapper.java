package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(uses = { CategoryMapper.class, AllergenMapper.class,
    IngredientMapper.class, NutritionMapper.class})
public interface RecipeMapper {


    /**
     * This method is responsible for mapping a list of recipe objects to a list of RecipeListDto objects.
     * It uses the @Mapping annotation to specify that the 'id' field of the recipe object should be mapped to the 'id' field of the RecipeBookListDto object.
     *
     * @param recipe A list of Recipe objects that need to be mapped to RecipeListDto objects.
     * @return A list of RecipeListDto objects that have been mapped from the provided list of RecipeBook objects.
     */
    List<RecipeListDto> recipeListToRecipeListDto(List<Recipe> recipe);

}
