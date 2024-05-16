package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface RecipeMapper {


    DetailedRecipeDto recipeToDetailedRecipeDto(Recipe recipe);

    default Recipe recipeCreateDtoToRecipe(RecipeCreateDto recipeCreateDto, long id) throws RecipeStepNotParsableException, RecipeStepSelfReferenceException {
        List<RecipeStep> recipeStepList = new ArrayList<>();
        int i = 1;
        for (RecipeStepDto recipeStepDto : recipeCreateDto.getSteps()) {
            RecipeStep recipeStep = new RecipeStep();
            recipeStep.setName(recipeStepDto.getName());
            recipeStep.setRecipeId(id);
            recipeStep.setStepNumber(i);
            if (!recipeStepDto.isCorrect()) {
                throw new RecipeStepNotParsableException("The steps in the Recipe are not formated correct!");
            }
            if (recipeStepDto.isWhichstep()) {
                RecipeDescriptionStep recipeDescriptionStep = new RecipeDescriptionStep();
                recipeDescriptionStep.setDescription(recipeStepDto.getDescription());
                recipeDescriptionStep.setName(recipeStepDto.getName());
                recipeStep.setStepDescription(recipeDescriptionStep);
            } else {
                if (recipeStepDto.getRecipeId() == id) {
                    throw new RecipeStepSelfReferenceException("A step references it's own recipe!");
                }
                RecipeRecipeStep recipeRecipeStep = new RecipeRecipeStep();
                recipeRecipeStep.setName(recipeStepDto.getName());
                recipeRecipeStep.setRecipeId(recipeStepDto.getRecipeId());
                recipeStep.setStepRecipe(recipeRecipeStep);
            }
            recipeStepList.add(recipeStep);
            i++;

        }
        List<RecipeIngredient> recipeIngredientList = recipeIngredientDtoToRecipeIngredientDto(recipeCreateDto.getIngredients());
        recipeIngredientList.forEach(obj -> obj.setRecipeId(id));

        Recipe.RecipeBuilder recipeBuilder = Recipe.RecipeBuilder.aRecipe();
        recipeBuilder.withName(recipeCreateDto.getName())
            .withDescription(recipeCreateDto.getDescription())
            .withNumberOfServings(recipeCreateDto.getServings())
            .withOwnerId(recipeCreateDto.getOwnerId())
            .withRecipeIngredients(recipeIngredientList)
            .withRecipeSteps(recipeStepList);

        return recipeBuilder.build();
    }

    ;

    List<RecipeIngredient> recipeIngredientDtoToRecipeIngredientDto(List<RecipeIngredientDto> recipeIngredientDto);

    default SimpleRecipeResultDto recipeToRecipeResultDto(Recipe r) {
        SimpleRecipeResultDto result = new SimpleRecipeResultDto();
        result.setRecipeId(r.getId());
        result.setWhichstep(false);
        result.setRecipename(r.getName());
        return result;
    }

    ;
}
