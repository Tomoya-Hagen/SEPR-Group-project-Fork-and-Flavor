package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
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
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
import at.ac.tuwien.sepr.groupphase.backend.service.validators.RecipeValidator;
import jakarta.transaction.Transactional;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class RecipeServiceImpl implements RecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final CategoryRepository categoryRepository;
    private final RecipeValidator recipeValidator;
    private final UserManager userManager;


    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeMapper recipeMapper,
                             CategoryRepository categoryRepository,
                             RecipeValidator recipeValidator,
                             UserManager userManager) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.categoryRepository = categoryRepository;
        this.recipeValidator = recipeValidator;
        this.userManager = userManager;
    }

    @Override
    public RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException {
        return getRecipeDetailDtoById(id, true);
    }

    @Override
    public RecipeDetailDto getRecipeDetailDtoById(long id, boolean recursive) throws NotFoundException {
        LOGGER.trace("getRecipeDetailDtoById({})", id);
        Recipe recipe = recipeRepository.getRecipeById(id).orElseThrow(NotFoundException::new);
        HashMap<Ingredient, RecipeIngredient> ingredients = new HashMap<>();
        HashMap<Nutrition, BigDecimal> nutritions = new HashMap<>();
        ArrayList<Allergen> allergens = new ArrayList<>();
        getRecipeDetails(recipe, ingredients, nutritions, allergens, recursive);
        long rating = calculateAverageTasteRating(recipe.getRatings());
        RecipeDetailDto result = recipeMapper.recipeToRecipeDetailDto(recipe, ingredients, nutritions, allergens, recipe.getOwner(), rating);

        List<Recipe> forkedRecipes = recipeRepository.findAllForkedRecipesById(id);
        ArrayList<String> forkedRecipeNames = new ArrayList<>();
        for (Recipe forkedRecipe : forkedRecipes) {
            forkedRecipeNames.add(forkedRecipe.getName());
        }
        return new RecipeDetailDto(
            result.id(),
            result.name(),
            result.description(),
            result.numberOfServings(),
            result.forkedFromId(),
            result.ownerId(),
            result.categories(),
            result.isDraft(),
            result.recipeSteps(),
            result.ingredients(),
            result.allergens(),
            result.nutritions(),
            forkedRecipeNames,
            result.rating()
        );
    }

    @Override
    public Page<RecipeListDto> getRecipesByName(String name, Pageable pageable) {
        Page<Recipe> recipePage = recipeRepository.findByNameContainingIgnoreCase(name, pageable);

        return recipePage.map(recipe -> {
            Long rating = calculateAverageTasteRating(recipe.getRatings());
            return recipeMapper.recipeToRecipeListDto(recipe, rating);
        });
    }

    @Override
    public Page<RecipeListDto> getRecipesThatGoWellWith(long id, Pageable pageable) throws NotFoundException {
        Recipe origRecipe = recipeRepository.getRecipeById(id).orElseThrow(NotFoundException::new);
        List<Recipe> goWellWith = origRecipe.getGoesWellWithRecipes();

        List<RecipeListDto> recipeListDtos = goWellWith.stream()
            .map(recipe -> {
                Long rating = calculateAverageTasteRating(recipe.getRatings());
                return recipeMapper.recipeToRecipeListDto(recipe, rating);
            })
            .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recipeListDtos.size());
        List<RecipeListDto> subList = recipeListDtos.subList(start, end);

        return new PageImpl<>(subList, pageable, recipeListDtos.size());
    }

    @Override
    public RecipeDetailDto addGoesWellWith(long id, List<RecipeListDto> goWellWith) throws ResponseStatusException {
        Recipe origRecipe = recipeRepository.getRecipeById(id).orElseThrow(NotFoundException::new);
        if (origRecipe.getOwner().getId() != userManager.getCurrentUser().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        List<Recipe> goWellWithRecipes = goWellWith.stream()
            .map(recipeListDto -> recipeRepository.getRecipeById(recipeListDto.id()).orElseThrow(NotFoundException::new))
            .collect(Collectors.toList());

        List<Recipe> uniqueRecipes = new ArrayList<>();
        for (Recipe recipe : goWellWithRecipes) {
            if (!origRecipe.getGoesWellWithRecipes().contains(recipe)) {
                uniqueRecipes.add(recipe);
            }
        }

        origRecipe.setGoesWellWithRecipes(uniqueRecipes);
        recipeRepository.save(origRecipe);

        return getRecipeDetailDtoById(id);
    }

    @Override
    public DetailedRecipeDto createRecipe(RecipeCreateDto recipeDto) throws ValidationException, RecipeStepNotParsableException, RecipeStepSelfReferenceException {
        LOGGER.debug("Publish new message {}", recipeDto);

        recipeValidator.validateCreate(recipeDto);

        Recipe simple = recipeMapper.recipeparsesimple(recipeDto);
        ApplicationUser owner = userManager.getCurrentUser();
        simple.setOwner(owner);
        recipeRepository.save(simple);

        Recipe recipe = recipeMapper.recipeCreateDtoToRecipe(recipeDto, simple.getId());

        List<Category> categories = new ArrayList<>();
        for (Category category : recipe.getCategories()) {
            categories.add(categoryRepository.getById(category.getId()));
        }
        simple.setIngredients(recipe.getIngredients());
        simple.setCategories(categories);
        simple.setRecipeSteps(recipe.getRecipeSteps());


        recipeRepository.save(simple);
        var x = recipeMapper.recipeToDetailedRecipeDto(simple);
        return x;
    }

    @Override
    public DetailedRecipeDto forkRecipe(RecipeCreateDto recipeDto, int forkid) throws ValidationException, RecipeStepNotParsableException, RecipeStepSelfReferenceException {
        LOGGER.debug("Publish new message {}", recipeDto);

        recipeValidator.validateCreate(recipeDto);

        Recipe simple = recipeMapper.recipeparsesimple(recipeDto);
        Recipe forked = recipeRepository.getRecipeById(forkid).orElseThrow(NotFoundException::new);
        simple.setForkedFrom(forked);
        ApplicationUser owner = userManager.getCurrentUser();
        simple.setOwner(owner);
        recipeRepository.save(simple);

        Recipe recipe = recipeMapper.recipeCreateDtoToRecipe(recipeDto, simple.getId());

        List<Category> categories = new ArrayList<>();
        for (Category category : recipe.getCategories()) {
            categories.add(categoryRepository.findById(category.getId()).orElseThrow(NotFoundException::new));
        }
        simple.setIngredients(recipe.getIngredients());
        simple.setCategories(categories);
        simple.setRecipeSteps(recipe.getRecipeSteps());


        recipeRepository.save(simple);
        var x = recipeMapper.recipeToDetailedRecipeDto(simple);
        return x;
    }

    @Override
    public Stream<SimpleRecipeResultDto> byname(String name, int limit) {
        return recipeRepository.findByNameContainingWithLimit(name, PageRequest.of(0, limit)).stream().map(recipeMapper::recipeToRecipeResultDto);
    }

    private long calculateAverageTasteRating(List<Rating> ratings) {
        LOGGER.trace("calculateAverageTasteRating({})", ratings);
        long rating = 0;
        if (!ratings.isEmpty()) {
            for (Rating value : ratings) {
                rating += (value.getTaste().longValue()
                    + value.getCost().longValue() + value.getEaseOfPrep().longValue()) / 3;
            }
            rating /= ratings.size();
        }
        return rating;
    }

    @Override
    public DetailedRecipeDto updateRecipe(@Valid RecipeUpdateDto recipeUpdateDto) {
        LOGGER.trace("updateRecipe({})", recipeUpdateDto);
        Recipe oldRecipe = recipeRepository.findById(recipeUpdateDto.id()).orElseThrow(NotFoundException::new);
        Recipe recipe = recipeMapper.recipeUpdateDtoToRecipe(recipeUpdateDto);
        if (oldRecipe.getOwner().getId() != userManager.getCurrentUser().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        List<RecipeIngredient> updatedIngredients = new ArrayList<>();

        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            boolean found = false;
            for (RecipeIngredient oldRecipeIngredient : oldRecipe.getIngredients()) {
                if (oldRecipeIngredient.getIngredient().getId().equals(recipeIngredient.getIngredient().getId())) {
                    oldRecipeIngredient.setUnit(recipeIngredient.getUnit());
                    oldRecipeIngredient.setAmount(recipeIngredient.getAmount());
                    updatedIngredients.add(oldRecipeIngredient);
                    found = true;
                    break;
                }
            }
            if (!found) {
                updatedIngredients.add(recipeIngredient);
            }
        }

        List<Category> categories = categoryRepository.findAllById(recipe.getCategories().stream().map(Category::getId).toList());

        oldRecipe.setCategories(categories);
        oldRecipe.setIngredients(updatedIngredients);
        oldRecipe.setName(recipe.getName());
        oldRecipe.setDescription(recipe.getDescription());
        oldRecipe.setNumberOfServings(recipe.getNumberOfServings());
        oldRecipe.setRecipeSteps(recipe.getRecipeSteps());

        recipeRepository.save(oldRecipe);

        return recipeMapper.recipeToDetailedRecipeDto(oldRecipe);
    }


    private void getRecipeDetails(
        Recipe recipe,
        Map<Ingredient, RecipeIngredient> ingredients,
        Map<Nutrition, BigDecimal> nutritions,
        List<Allergen> allergens,
        boolean recursive) {
        LOGGER.trace("getRecipeDetails({}, {}, {}, {})",
            recipe, ingredients, nutritions, allergens);
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            Ingredient ingredient = recipeIngredient.getIngredient();
            updateMapOfIngredients(recipeIngredient, ingredient, ingredients);
            updateMapOfNutritions(ingredient, nutritions);
            updateListOfAllergens(ingredient, allergens);
        }
        List<RecipeStep> recipeSteps = recipe.getRecipeSteps();
        recipeSteps.sort(Comparator.comparing(RecipeStep::getStepNumber));
        if (recursive) {
            for (RecipeStep recipeStep : recipeSteps) {
                if (recipeStep instanceof RecipeRecipeStep recipeRecipeStep) {
                    getRecipeDetails(recipeRecipeStep.getRecipeRecipe(), ingredients,
                        nutritions, allergens, recursive);
                }
            }
        }
    }

    private void updateMapOfIngredients(RecipeIngredient recipeIngredient, Ingredient ingredient, Map<Ingredient, RecipeIngredient> ingredients) {
        LOGGER.trace("updateMapOfIngredients({}, {}, {})",
            recipeIngredient, ingredients, ingredients);
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
        LOGGER.trace("updateMapOfNutritions({}, {})",
            ingredient, nutritions);
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
        LOGGER.trace("getRecipeDetails({}, {})",
            ingredient, allergens);
        for (Allergen allergen : ingredient.getAllergens()) {
            if (!allergens.contains(allergen)) {
                allergens.add(allergen);
            }
        }
    }
}
