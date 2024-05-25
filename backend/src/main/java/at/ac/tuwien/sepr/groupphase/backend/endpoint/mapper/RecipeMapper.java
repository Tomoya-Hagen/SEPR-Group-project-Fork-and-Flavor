package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper(uses = {RecipeStepMapper.class, CategoryMapper.class, AllergenMapper.class,
    IngredientMapper.class, NutritionMapper.class})
public interface RecipeMapper {
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

    @Mapping(source = "recipe.name", target = "name")
    @Mapping(source = "recipe.description", target = "description")
    RecipeListDto recipeAndAverageRatingToRecipeListDto(Recipe recipe, long rating);

    DetailedRecipeDto recipeToDetailedRecipeDto(Recipe recipe);

    default Recipe recipeUpdateDtoToRecipe(RecipeUpdateDto recipeUpdateDto) throws RecipeStepNotParsableException, RecipeStepSelfReferenceException {
        Recipe current = new Recipe();
        Long id = recipeUpdateDto.id();
        current.setId(id);

        List<RecipeStep> recipeStepList = new ArrayList<>();
        int i = 1;
        for (RecipeStepDto recipeStepDto : recipeUpdateDto.recipeSteps()) {
            RecipeStep recipeStep;
            if (!recipeStepDto.isCorrect()) {
                throw new RecipeStepNotParsableException("The steps in the Recipe are not formated correctly!");
            }
            if (recipeStepDto.isWhichstep()) {
                recipeStep = new RecipeDescriptionStep(recipeStepDto.getName(), recipeStepDto.getDescription(), current, i);
            } else {
                if (recipeStepDto.getRecipeId() == id) {
                    throw new RecipeStepSelfReferenceException("A step references its own recipe!");
                }
                Recipe r = new Recipe();
                r.setId(recipeStepDto.getRecipeId());
                recipeStep = new RecipeRecipeStep(recipeStepDto.getName(), current, i, r);
            }
            recipeStepList.add(recipeStep);
            i++;
        }
        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        for (RecipeIngredientDto recipeIngredient : recipeUpdateDto.ingredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(recipeIngredient.getId());
            RecipeIngredient.Unit u =  RecipeIngredient.getUnitFromString(recipeIngredient.getUnit());
            RecipeIngredient recipeIngr = new RecipeIngredient(current, ingredient, recipeIngredient.getAmount(), u);
            recipeIngredientList.add(recipeIngr);
        }

        List<Category> categoryList = new ArrayList<>();
        for (RecipeCategoryDto categoryDto : recipeUpdateDto.categories()) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            categoryList.add(category);
        }

        Recipe ret = new Recipe();
        ret.setId(id);
        ret.setName(recipeUpdateDto.name());
        ret.setDescription(recipeUpdateDto.description());
        ret.setNumberOfServings(recipeUpdateDto.numberOfServings());
        ret.setIngredients(recipeIngredientList);
        ret.setRecipeSteps(recipeStepList);
        ret.setCategories(categoryList);
        return ret;
    }

}
