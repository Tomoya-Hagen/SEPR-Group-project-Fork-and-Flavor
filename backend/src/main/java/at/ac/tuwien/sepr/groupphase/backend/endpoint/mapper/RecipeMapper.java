package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
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

/**
 * This mapper is used to map recipes zu all kinds of different dto types.
 */
@Mapper(uses = {RecipeStepMapper.class, CategoryMapper.class, AllergenMapper.class, IngredientMapper.class, NutritionMapper.class})
public interface RecipeMapper {

    /**
     * This method creates a RecipeDetailDto out of the data of the recipe.
     *
     * @param recipe      represents the entity in the data storage.
     * @param ingredients represents the ingredients of the given recipe with the used amount.
     * @param nutritions  represents the nutrition data of the given recipe.
     * @param allergens   represents the allergens that the given recipe contains.
     * @param owner       represents the owner of the given recipe.
     * @param rating      represents the average taste rating of the given recipe.
     * @return a RecipeDetailDto which contains the given parameters.
     */
    @Mapping(source = "ingredients", target = "ingredients")
    @Mapping(source = "nutritions", target = "nutritions")
    @Mapping(source = "allergens", target = "allergens")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "recipe.id", target = "id")
    @Mapping(source = "recipe.isDraft", target = "isDraft")
    @Mapping(source = "recipe.forkedFrom", target = "forkedFrom")
    RecipeDetailDto recipeToRecipeDetailDto(Recipe recipe, HashMap<Ingredient, RecipeIngredient> ingredients, HashMap<Nutrition, BigDecimal> nutritions, ArrayList<Allergen> allergens, ApplicationUser owner, long rating, int validations);

    /**
     * Converts a list of Recipe entities and their corresponding ratings to a list of RecipeListDto objects.
     *
     * @param recipes The list of Recipe entities.
     * @param ratings The list of corresponding ratings for each Recipe.
     * @return A list of RecipeListDto objects.
     */
    default ArrayList<RecipeListDto> recipeListAndRatingListToListOfRecipeRatingDto(ArrayList<Recipe> recipes, ArrayList<Long> ratings) {
        ArrayList<RecipeListDto> recipeListDtos = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            recipeListDtos.add(recipeAndAverageRatingToRecipeListDto(recipes.get(i), ratings.get(i)));
        }
        return recipeListDtos;
    }

    /**
     * Converts a list of RecipeListDto objects to a list of Recipe entities.
     *
     * @param recipeListDto The list of RecipeListDto objects.
     * @return A list of Recipe entities.
     */
    @Mapping(source = "recipeListDto.id", target = "id")
    default List<Recipe> listOfRecipeListDtoToRecipeList(List<RecipeListDto> recipeListDto) {
        List<Recipe> recipeList = new ArrayList<>();
        for (RecipeListDto recipeListDto1 : recipeListDto) {
            recipeList.add(recipeListDtoToRecipe(recipeListDto1));
        }
        return recipeList;
    }

    /**
     * This method creates a RecipeListDto out of a recipe entity and the average rating based on the taste.
     *
     * @param recipe represents the entity of a recipe.
     * @param rating is the average rating of the given recipe based on the taste.
     * @return a RecipeListDto based on the given parameters.
     */
    @Mapping(source = "recipe.name", target = "name")
    @Mapping(source = "recipe.description", target = "description")
    RecipeListDto recipeAndAverageRatingToRecipeListDto(Recipe recipe, long rating);

    /**
     * Converts a Recipe entity to a DetailedRecipeDto object.
     *
     * @param recipe The Recipe entity.
     * @return A DetailedRecipeDto object.
     */
    DetailedRecipeDto recipeToDetailedRecipeDto(Recipe recipe);

    /**
     * Converts a RecipeUpdateDto object to a Recipe entity.
     *
     * @param recipeUpdateDto The RecipeUpdateDto object.
     * @return A Recipe entity.
     * @throws RecipeStepNotParsableException   If the steps in the Recipe are not formatted correctly.
     * @throws RecipeStepSelfReferenceException If a step references its own recipe.
     */
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
            RecipeIngredient.Unit u = RecipeIngredient.getUnitFromString(recipeIngredient.getUnit());
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

    /**
     * Converts a Recipe entity to a SimpleRecipeResultDto object.
     *
     * @param r The Recipe entity.
     * @return A SimpleRecipeResultDto object.
     */
    default SimpleRecipeResultDto recipeToRecipeResultDto(Recipe r) {
        SimpleRecipeResultDto result = new SimpleRecipeResultDto();
        result.setRecipeId(r.getId());
        result.setWhichstep(false);
        result.setRecipename(r.getName());
        return result;
    }

    /**
     * Converts a list of Recipe entities to a list of RecipeListDto objects.
     *
     * @param recipes The list of Recipe entities.
     * @return A list of RecipeListDto objects.
     */
    List<RecipeListDto> recipesToRecipeListDto(List<Recipe> recipes);

    /**
     * Converts a RecipeCreateDto object and an id to a Recipe entity.
     *
     * @param recipeCreateDto The RecipeCreateDto object.
     * @param id              The id.
     * @return A Recipe entity.
     * @throws RecipeStepNotParsableException   If the steps in the Recipe are not formatted correctly.
     * @throws RecipeStepSelfReferenceException If a step references its own recipe.
     */
    default Recipe recipeCreateDtoToRecipe(RecipeCreateDto recipeCreateDto, long id) throws RecipeStepNotParsableException, RecipeStepSelfReferenceException {
        Recipe current = new Recipe();
        current.setId(id);

        List<RecipeStep> recipeStepList = new ArrayList<>();
        int i = 1;
        for (RecipeStepDto recipeStepDto : recipeCreateDto.getRecipeSteps()) {
            RecipeStep recipeStep;
            if (!recipeStepDto.isCorrect()) {
                throw new RecipeStepNotParsableException("The steps in the Recipe are not formated correct!");
            }
            if (recipeStepDto.isWhichstep()) {
                recipeStep = new RecipeDescriptionStep(recipeStepDto.getName(), recipeStepDto.getDescription(), current, i);

            } else {
                if (recipeStepDto.getRecipeId() == id) {
                    throw new RecipeStepSelfReferenceException("A step references it's own recipe!");
                }
                Recipe reciper = new Recipe();
                reciper.setId(recipeStepDto.getRecipeId());
                recipeStep = new RecipeRecipeStep(recipeStepDto.getName(), current, i, reciper);
            }
            recipeStepList.add(recipeStep);
            i++;

        }
        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        for (RecipeIngredientDto recipeIngredient : recipeCreateDto.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(recipeIngredient.getId());
            RecipeIngredient.Unit u = RecipeIngredient.getUnitFromString(recipeIngredient.getUnit());
            RecipeIngredient recieing = new RecipeIngredient(current, ingredient, recipeIngredient.getAmount(), u);
            recipeIngredientList.add(recieing);
        }

        List<Category> categoryList = new ArrayList<>();
        for (RecipeCategoryDto categoryDto : recipeCreateDto.getCategories()) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            categoryList.add(category);
        }

        Recipe ret = new Recipe();
        ret.setIngredients(recipeIngredientList);
        ret.setRecipeSteps(recipeStepList);
        ret.setCategories(categoryList);
        return ret;
    }

    /**
     * Converts a RecipeCreateDto object to a Recipe entity.
     *
     * @param recipeCreateDto The RecipeCreateDto object.
     * @return A Recipe entity.
     */
    default Recipe recipeparsesimple(RecipeCreateDto recipeCreateDto) {
        Recipe ret = new Recipe();
        ret.setName(recipeCreateDto.getName());
        ret.setDescription(recipeCreateDto.getDescription());
        ret.setNumberOfServings(recipeCreateDto.getNumberOfServings());
        ApplicationUser owner = new ApplicationUser();
        ret.setOwner(owner);
        return ret;
    }

    /**
     * Converts a list of Recipe entities to a list of RecipeListDto objects.
     *
     * @param recipe The list of Recipe entities.
     * @return A list of RecipeListDto objects.
     */
    List<RecipeListDto> recipeListToRecipeListDto(List<Recipe> recipe);

    /**
     * Converts a RecipeListDto object to a Recipe entity.
     *
     * @param recipeListDto The RecipeListDto object.
     * @return A Recipe entity.
     */
    Recipe recipeListDtoToRecipe(RecipeListDto recipeListDto);

    /**
     * Converts a Recipe entity and a rating to a RecipeListDto object.
     *
     * @param recipe The Recipe entity.
     * @param rating The rating.
     * @return A RecipeListDto object.
     */
    RecipeListDto recipeToRecipeListDto(Recipe recipe, Long rating);
}
