package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface RecipeMapper {

    @Mapping(source = "recipeListDto.id", target = "id")
    default List<Recipe> ListOfRecipeListDtoToRecipeList(List<RecipeListDto> recipeListDto) {
        List<Recipe> recipeList = new ArrayList<>();
        for (RecipeListDto recipeListDto1 : recipeListDto) {
            recipeList.add(recipeListDtoToRecipe(recipeListDto1));
        }
        return recipeList;
    }

    Recipe recipeListDtoToRecipe(RecipeListDto recipeListDto);
}
