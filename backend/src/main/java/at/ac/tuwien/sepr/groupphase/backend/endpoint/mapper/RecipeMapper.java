package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RecipeMapper {
    @Mapping(source = "recipe.name", target = "name")
    RecipeListDto recipeToRecipeListDto(Recipe recipe);

}
