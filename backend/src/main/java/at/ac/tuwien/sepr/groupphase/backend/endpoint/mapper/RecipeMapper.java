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
@Mapper(uses = {RecipeStepMapper.class, CategoryMapper.class, AllergenMapper.class,
        IngredientMapper.class, NutritionMapper.class})
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
    @Mapping(source = "recipe.forkedFrom.id", target = "forkedFromId")
    RecipeDetailDto recipeToRecipeDetailDto(Recipe recipe, HashMap<Ingredient,
            RecipeIngredient> ingredients, HashMap<Nutrition, BigDecimal> nutritions, ArrayList<Allergen> allergens, ApplicationUser owner, long rating, int verifications);


    default ArrayList<RecipeListDto> recipeListAndRatingListToListOfRecipeRatingDto(ArrayList<Recipe> recipes, ArrayList<Long> ratings) {
        ArrayList<RecipeListDto> recipeListDtos = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            recipeListDtos.add(recipeAndAverageRatingToRecipeListDto(recipes.get(i), ratings.get(i)));
        }
        return recipeListDtos;
    }

    /**
     * This method creates a list of recipe out of a list of recipeListDto.
     *
     * @param recipeListDto represents the recipeListDto that should be converted to a list of recipe.
     * @return the list of recipeLiatDto
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
     * This method creates a DetailedRecipeDto out of a recipe entity.
     *
     * @param recipe represents the recipe that should be converted to DetailedRecipeDto.
     * @return a DetailedRecipe
     */
    DetailedRecipeDto recipeToDetailedRecipeDto(Recipe recipe);

    /**
     * This method creates a list of recipe out of a list of recipeListDto.
     *
     * @param recipeUpdateDto represents the recipeUpdateDto that should be converted to a recipe.
     * @return the updated recipe
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

    /**
     * This method creates a simpleRecipeResultDto out of recipe.
     *
     * @param r represents the recipe that should be converted to a simpleRecipeResultDto.
     * @return the simpleRecipeResultDto
     */
    default SimpleRecipeResultDto recipeToRecipeResultDto(Recipe r) {
        SimpleRecipeResultDto result = new SimpleRecipeResultDto();
        result.setRecipeId(r.getId());
        result.setWhichstep(false);
        result.setRecipename(r.getName());
        return result;
    }

    /**
     * This method creates a list of recipeListDto out of a list of recipe.
     *
     * @param recipes represents the list of recipe that should be converted to a list of recipeListDto.
     * @return the list of recipeLiatDto
     */
    List<RecipeListDto> recipesToRecipeListDto(List<Recipe> recipes);

    /**
     * This method creates a recipe out of a recipeCreateDto and id.
     *
     * @param recipeCreateDto represents the recipeCreateDto that should be converted to recipe
     * @param id represents the recipe id that should be converted to new recipe
     * @return a created recipe
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
     * This method creates a recipe out of a recipeCreateDto.
     *
     * @param recipeCreateDto represents the recipeCreateDto that should be converted to a recipe.
     * @return a simple recipe
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

    List<RecipeListDto> recipeListToRecipeListDto(List<Recipe> recipe);

    /**
     * This method creates a recipe out of a recipeListDto.
     *
     * @param recipeListDto represents the recipeListDto that should be converted to a recipe.
     * @return a recipe
     */
    Recipe recipeListDtoToRecipe(RecipeListDto recipeListDto);

    /**
     * This method creates a recipeListDto out of a recipe and a rating.
     *
     * @param recipe represents the recipe that should be converted to a recipeListDto
     * @param rating represents the rating that should be converted to a recipeListDto
     * @return a recipeListDto
     */
    RecipeListDto recipeToRecipeListDto(Recipe recipe, Long rating);
}
