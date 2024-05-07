package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeCategory;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface RecipeMapper {


    DetailedRecipeDto recipeToDetailedRecipeDto(Recipe recipe);

    default Recipe recipeCreateDtoToRecipe(RecipeCreateDto recipeCreateDto, long id){
        List<RecipeCategory> recipeCategoryList =recipeCategoryDtoToRecipeCategory(recipeCreateDto.getRecipeCategories());
        recipeCategoryList.forEach(obj -> obj.setRecipeId(id));
        List<RecipeStep> recipeStepList = recipeStepToRecipeStepDto(recipeCreateDto.getRecipeSteps());
        List<RecipeIngredient> recipeIngredientList = recipeIngredientDtoToRecipeIngredientDto(recipeCreateDto.getRecipeIngredients());

        Recipe.RecipeBuilder recipeBuilder = Recipe.RecipeBuilder.aRecipe();
        recipeBuilder.withName(recipeCreateDto.getName())
            .withDescription(recipeCreateDto.getDescription())
            .withNumberOfServings(recipeCreateDto.getNumberOfServings())
            .withOwnerId(recipeCreateDto.getOwnerId())
            .withRecipeIngredients(recipeIngredientList)
            .withRecipeCategories(recipeCategoryList)
            .withRecipeSteps(recipeStepList);

            return recipeBuilder.build();
    };


    List<RecipeIngredient> recipeIngredientDtoToRecipeIngredientDto(List<RecipeIngredientDto> recipeIngredientDto);
    List<RecipeCategory>  recipeCategoryDtoToRecipeCategory(List<RecipeCategoryDto> recipeCategoryDto);
    List<RecipeStep> recipeStepToRecipeStepDto (List<RecipeStepDto> recipeStepDto);
}
