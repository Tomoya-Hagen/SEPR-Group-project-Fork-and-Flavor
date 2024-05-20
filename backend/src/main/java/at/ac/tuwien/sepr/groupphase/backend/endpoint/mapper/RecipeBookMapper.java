package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.*;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(uses = {RecipeStepMapper.class, CategoryMapper.class, AllergenMapper.class,
    IngredientMapper.class, NutritionMapper.class})
public interface RecipeBookMapper {
    List<RecipeBookListDto> recipeBookListToRecipeBookListDto(List<RecipeBook> recipeBook);
}
