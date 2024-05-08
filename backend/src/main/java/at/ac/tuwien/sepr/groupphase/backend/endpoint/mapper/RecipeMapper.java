package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeCategory;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface RecipeMapper  {


    DetailedRecipeDto recipeToDetailedRecipeDto(Recipe recipe);

    default Recipe recipeCreateDtoToRecipe(RecipeCreateDto recipeCreateDto, long id) throws RecipeStepNotParsableException {
        List<RecipeCategory> recipeCategoryList =recipeCategoryDtoToRecipeCategory(recipeCreateDto.getRecipeCategories());
        recipeCategoryList.forEach(obj -> obj.setRecipeId(id));
        List<RecipeStep> recipeStepList = new ArrayList<>();
        for(RecipeStepDto recipeStepDto : recipeCreateDto.getRecipeSteps()){
            RecipeStep recipeStep = new RecipeStep();
            recipeStep.setName(recipeStepDto.getName());
            recipeStep.setRecipeId(id);
            recipeStep.setStepNumber(recipeStepDto.getStepNumber());

            if(!recipeStepDto.isCorrect()){
                throw new RecipeStepNotParsableException();
            }
            if(recipeStepDto.isWhichstep()){
                RecipeDescriptionStep recipeDescriptionStep = new RecipeDescriptionStep();
                recipeDescriptionStep.setDescription(recipeStepDto.getDescription());
                recipeDescriptionStep.setName(recipeStepDto.getRecipename());
                recipeStep.setStepDescription(recipeDescriptionStep);
            }
            else {
                RecipeRecipeStep recipeRecipeStep = new RecipeRecipeStep();
                recipeRecipeStep.setName(recipeStepDto.getRecipename());
                recipeRecipeStep.setRecipeId(recipeStepDto.getRecipeId());
                recipeStep.setStepRecipe(recipeRecipeStep);
            }
            recipeStepList.add(recipeStep);

        }
        List<RecipeIngredient> recipeIngredientList = recipeIngredientDtoToRecipeIngredientDto(recipeCreateDto.getRecipeIngredients());
        recipeIngredientList.forEach(obj -> obj.setRecipeId(id));

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
