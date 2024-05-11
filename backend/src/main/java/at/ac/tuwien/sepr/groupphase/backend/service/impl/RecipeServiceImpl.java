package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AllergenDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NutritionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AllergenMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.IngredientMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.NutritionMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeStepMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.IngredientNutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientMapper ingredientMapper;
    private final RecipeMapper recipeMapper;
    private final NutritionMapper nutritionMapper;
    private final AllergenMapper allergenMapper;
    private final RecipeStepMapper recipeStepMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeMapper recipeMapper,
                             IngredientMapper ingredientMapper,
                             NutritionMapper nutritionMapper,
                             AllergenMapper allergenMapper,
                             RecipeStepMapper recipeStepMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.ingredientMapper = ingredientMapper;
        this.nutritionMapper = nutritionMapper;
        this.allergenMapper = allergenMapper;
        this.recipeStepMapper = recipeStepMapper;
    }

    @Override
    public RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException {
        Recipe recipe = recipeRepository.getRecipeById(id).orElseThrow(NotFoundException::new);
        HashMap<Ingredient, RecipeIngredient> ingredients = new HashMap<>();
        HashMap<Nutrition, BigDecimal> nutritions = new HashMap<>();
        ArrayList<Allergen> allergens = new ArrayList<>();
        getRecipeDetails(recipe, ingredients, nutritions, allergens);
        List<AllergenDetailDto> allergenDetailDtos = allergenMapper.allergenListToAllergenDetailDtoList(allergens);
        List<NutritionDetailDto> nutritionDetailDtos = new ArrayList<>();
        List<IngredientDetailDto> ingredientDetailDtos = new ArrayList<>();
        List<RecipeStepDetailDto> recipeStepDetailDtos = recipeStepMapper
            .recipeStepsListToRecipeStepDetailDtoList(recipe.getRecipeSteps());
        for (Nutrition nutrition : nutritions.keySet()) {
            nutritionDetailDtos.add(
                nutritionMapper.nutritionAndValueToNutritionDetailDto(nutrition, nutritions.get(nutrition))
            );
        }
        for (Ingredient ingredient : ingredients.keySet()) {
            ingredientMapper.ingredientAndRecipeIngredientToIngredientDetailDto(ingredients.get(ingredient), ingredient);
        }
        return recipeMapper.recipeToRecipeDetailDto(recipe, ingredientDetailDtos,
            nutritionDetailDtos, allergenDetailDtos, recipeStepDetailDtos);
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
        for (RecipeStep recipeStep : recipe.getRecipeSteps()) {
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
            if (allergens.contains(allergen)) {
                allergens.add(allergen);
            }
        }
    }
}
