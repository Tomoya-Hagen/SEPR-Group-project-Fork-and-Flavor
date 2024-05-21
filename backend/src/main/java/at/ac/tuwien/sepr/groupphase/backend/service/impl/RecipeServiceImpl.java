package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.IngredientNutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Rating;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeIngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeStepRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Transactional
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeMapper recipeMapper, UserRepository userRepository,
                             CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException {
        Recipe recipe = recipeRepository.getRecipeById(id).orElseThrow(NotFoundException::new);
        HashMap<Ingredient, RecipeIngredient> ingredients = new HashMap<>();
        HashMap<Nutrition, BigDecimal> nutritions = new HashMap<>();
        ArrayList<Allergen> allergens = new ArrayList<>();
        getRecipeDetails(recipe, ingredients, nutritions, allergens);
        long rating = calculateAverageTasteRating(recipe.getRatings());
        return recipeMapper.recipeToRecipeDetailDto(recipe, ingredients, nutritions, allergens, recipe.getOwner(), rating);
    }

    @Override
    public ArrayList<RecipeListDto> getRecipesFromPageInSteps(int pageNumber, int stepNumber) {
        int from = ((pageNumber - 1) * stepNumber) + 1;
        int to = pageNumber * stepNumber;
        ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeRepository.getAllRecipesWithIdFromTo(from, to);
        ArrayList<Long> ratings = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ratings.add(calculateAverageTasteRating(recipe.getRatings()));
        }
        return recipeMapper.recipeListAndRatingListToListOfRecipeRatingDto(recipes, ratings);
    }

    @Override
    public DetailedRecipeDto createRecipe(RecipeCreateDto recipeDto, String usermail) throws RecipeStepNotParsableException, RecipeStepSelfReferenceException {
        LOGGER.debug("Publish new message {}", recipeDto);

        Recipe recipe = recipeMapper.recipeCreateDtoToRecipe(recipeDto, recipeRepository.findMaxId() + 1);

        List<Category> categories = new ArrayList<>();
        for(Category category : recipe.getCategories()) {
            categories.add(categoryRepository.getById(category.getId()));
        }
        recipe.setCategories(categories);

        ApplicationUser owner = userRepository.findFirstUserByEmail(usermail);
        recipe.setOwner(owner);
        recipeRepository.save(recipe);

        return recipeMapper.recipeToDetailedRecipeDto(recipe);
    }

    @Override
    public Stream<SimpleRecipeResultDto> byname(String name, int limit) {
        var x = recipeRepository.findByNameContainingWithLimit(name, PageRequest.of(0,limit)).stream().map(recipeMapper::recipeToRecipeResultDto);
        return x;
    }

    private long calculateAverageTasteRating(List<Rating> ratings) {
        long rating = 0;
        if (!ratings.isEmpty()) {
            for (Rating value : ratings) {
                rating += value.getTaste().longValue();
            }
            rating /= ratings.size();
        }
        return rating;
    }

    private void getRecipeDetails(
        Recipe recipe,
        Map<Ingredient, RecipeIngredient> ingredients,
        Map<Nutrition, BigDecimal> nutritions,
        List<Allergen> allergens) {
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            Ingredient ingredient = recipeIngredient.getIngredient();
            updateMapOfIngredients(recipeIngredient, ingredient, ingredients);
            updateMapOfNutritions(ingredient, nutritions);
            updateListOfAllergens(ingredient, allergens);
        }
        List<RecipeStep> recipeSteps = recipe.getRecipeSteps();
        recipeSteps.sort(Comparator.comparing(RecipeStep::getStepNumber));
        for (RecipeStep recipeStep : recipeSteps) {
            if (recipeStep instanceof RecipeRecipeStep recipeRecipeStep) {
                getRecipeDetails(recipeRecipeStep.getRecipeRecipe(), ingredients,
                    nutritions, allergens);
            }
        }
    }

    private void updateMapOfIngredients(RecipeIngredient recipeIngredient, Ingredient ingredient, Map<Ingredient, RecipeIngredient> ingredients) {
        if (ingredients.containsKey(ingredient)) {
            RecipeIngredient temp = ingredients.get(ingredient);
            temp.setAmount(temp.getAmount().add(recipeIngredient.getAmount()));
            ingredients.put(ingredient, temp);
        } else {
            ingredients.put(recipeIngredient.getIngredient(),
                new RecipeIngredient(null, null,
                    recipeIngredient.getAmount(), recipeIngredient.getUnit()));
        }
    }

    private void updateMapOfNutritions(Ingredient ingredient, Map<Nutrition, BigDecimal> nutritions) {
        for (IngredientNutrition ingredientNutrition : ingredient.getNutritions()) {
            if (nutritions.containsKey(ingredientNutrition.getNutrition())) {
                nutritions.put(ingredientNutrition.getNutrition(),
                    nutritions.get(ingredientNutrition.getNutrition())
                        .add(ingredientNutrition.getValue()));
            } else {
                nutritions.put(ingredientNutrition.getNutrition(), ingredientNutrition.getValue());
            }
        }
    }

    private void updateListOfAllergens(Ingredient ingredient, List<Allergen> allergens) {
        for (Allergen allergen : ingredient.getAllergens()) {
            if (!allergens.contains(allergen)) {
                allergens.add(allergen);
            }
        }
    }
}
