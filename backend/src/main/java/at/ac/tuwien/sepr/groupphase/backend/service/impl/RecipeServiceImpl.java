package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecommendEvaluation;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeSearchDto;
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
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RatingRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.BadgeService;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepr.groupphase.backend.service.Roles;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
import at.ac.tuwien.sepr.groupphase.backend.service.validators.RecipeValidator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.AbstractMap;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.DoubleAdder;
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
    private final BadgeService badgeService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeMapper recipeMapper,
                             CategoryRepository categoryRepository,
                             RecipeValidator recipeValidator,
                             UserManager userManager,
                             BadgeService badgeService,
                             EmailService emailService,
                             UserRepository userRepository,
                             RatingRepository ratingRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.categoryRepository = categoryRepository;
        this.recipeValidator = recipeValidator;
        this.userManager = userManager;
        this.badgeService = badgeService;
        this.emailService = emailService;
        this.userRepository = userRepository;
      this.ratingRepository = ratingRepository;
    }

    @Override
    public RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException {
        return getRecipeDetailDtoById(id, true);
    }

    @Override
    public RecipeDetailDto getRecipeDetailDtoById(long id, boolean recursive) throws NotFoundException {
        LOGGER.trace("getRecipeDetailDtoById({})", id);
        Recipe recipe = recipeRepository.getRecipeById(id).orElseThrow(NotFoundException::new);
        int verifications = recipe.getVerifiers().size();
        HashMap<Ingredient, RecipeIngredient> ingredients = new HashMap<>();
        HashMap<Nutrition, BigDecimal> nutritions = new HashMap<>();
        ArrayList<Allergen> allergens = new ArrayList<>();
        getRecipeDetails(recipe, ingredients, nutritions, allergens, recursive);
        long rating = calculateAverageTasteRating(recipe.getRatings());
        RecipeDetailDto result = recipeMapper.recipeToRecipeDetailDto(recipe, ingredients, nutritions, allergens, recipe.getOwner(), rating, verifications);

        List<Recipe> forkedRecipes = recipeRepository.findAllForkedRecipesById(id);
        ArrayList<RecipeListDto> forkedRecipeNames = new ArrayList<>();
        for (Recipe forkedRecipe : forkedRecipes) {
            forkedRecipeNames.add(
              new RecipeListDto(
                forkedRecipe.getId(),
                forkedRecipe.getName(),
                forkedRecipe.getDescription(),
                calculateAverageTasteRating(forkedRecipe.getRatings())
              )
            );
        }

        return new RecipeDetailDto(
                result.id(),
                result.name(),
                result.description(),
                result.numberOfServings(),
                result.forkedFrom(),
                result.ownerId(),
                result.categories(),
                result.isDraft(),
                result.recipeSteps(),
                result.ingredients(),
                result.allergens(),
                result.nutritions(),
                forkedRecipeNames,
                result.rating(),
                result.verifications()
        );
    }

    @Override
    public Page<RecipeListDto> getRecipesByName(String name, Pageable pageable) {
        Page<Recipe> recipePage = recipeRepository.findByNameContainingIgnoreCaseOrderByName(name, pageable);

        return recipePage.map(recipe -> {
            Long rating = calculateAverageTasteRating(recipe.getRatings());
            return recipeMapper.recipeToRecipeListDto(recipe, rating);
        });
    }

    @Override
    public Page<RecipeListDto> getRecipesByNameCategories(RecipeSearchDto searchDto, Pageable pageable) {
        Page<Recipe> recipePage = recipeRepository.findByCategoryIdContainingIgnoreCaseOrderByName(searchDto.name(), searchDto.categorieId(), pageable);

        return recipePage.map(recipe -> {
            Long rating = calculateAverageTasteRating(recipe.getRatings());
            return recipeMapper.recipeToRecipeListDto(recipe, rating);
        });
    }

    @Override
    public void verifyRecipe(long recipeId) throws ValidationException {
        LOGGER.trace("verifyRecipe({})", recipeId);
        ApplicationUser user = userManager.getCurrentUser();
        if (!userManager.hasUserRole(user, Roles.StarCook)) {
            throw new ForbiddenException();
        }
        Recipe recipe = recipeRepository.getRecipeById(recipeId).orElseThrow(NotFoundException::new);
        if (recipeRepository.getVerifysByRecipeIdAndUserId(recipe.getId(), user.getId()).isPresent()) {
            throw new DuplicateObjectException("A verification to this recipe already exists");
        }
        if (user.equals(recipe.getOwner())) {
            throw new ValidationException("Owner can not verify recipe", List.of());
        }
        List<ApplicationUser> verifiers = recipe.getVerifiers();
        verifiers.add(user);
        recipe.setVerifiers(verifiers);

        recipeRepository.save(recipe);
        emailService.sendSimpleEmail(recipe.getOwner().getEmail(), "Rezept Verifikation",
                "Dein Rezept " + recipe.getName()
                        + " wurde verifiziert.");
    }

    @Override
    public boolean hasVerified(long recipeId) {
        LOGGER.trace("hasVerified({})", recipeId);
        ApplicationUser user = userManager.getCurrentUser();
        Recipe recipe = recipeRepository.getRecipeById(recipeId).orElseThrow(NotFoundException::new);
        return recipe.getVerifiers().contains(user);
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
        badgeService.addRoleToUser(owner, Roles.Cook);
        badgeService.addRoleToUser(owner, Roles.StarCook);
        return recipeMapper.recipeToDetailedRecipeDto(simple);
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
        badgeService.addRoleToUser(owner, Roles.Cook);
        return recipeMapper.recipeToDetailedRecipeDto(simple);
    }

    @Override
    public Stream<SimpleRecipeResultDto> byname(String name, int limit) {
        return recipeRepository.findByNameContainingWithLimit(name, PageRequest.of(0, limit)).stream().map(recipeMapper::recipeToRecipeResultDto);
    }

    @Override
    public Stream<SimpleRecipeResultDto> bynamecategories(RecipeSearchDto searchDto, int limit) {
        return recipeRepository.findByNameContainingWithLimit(searchDto.name(), searchDto.categorieId(), PageRequest.of(0, limit)).stream().map(recipeMapper::recipeToRecipeResultDto);
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

    @Override
    public List<RecipeListDto> getRecipesByRecommendation() {
        List<Recipe> result = this.trainmodel();
        return recipeMapper.recipesToRecipeListDto(result);
    }

    public List<Recipe> trainmodel() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        HashMap<ApplicationUser, HashMap<Ingredient, Integer>> all = new HashMap();
        HashMap<ApplicationUser, List<Recipe>> compareMap = new HashMap();
        List<ApplicationUser> users = userRepository.findAll();
        for (ApplicationUser user : users) {
            Dictionary<ApplicationUser, RecommendEvaluation> map = new Hashtable<>();
            HashMap<Ingredient, Integer> ingredients = new HashMap<>();
            var recipes = recipeRepository.findAllRecipesByGoodInteraction(user);
            compareMap.put(user, recipes);
            for (Recipe recipe : recipes) {
                for (RecipeIngredient ingredient : recipe.getIngredients()) {
                    if (ingredients.containsKey(ingredient.getIngredient())) {
                        ingredients.put(ingredient.getIngredient(), ingredients.get(ingredient.getIngredient()) + 1);
                    } else {
                        ingredients.put(ingredient.getIngredient(), 1);
                    }
                }
            }
            all.put(user, ingredients);
        }
        HashMap<ApplicationUser, List<RecommendEvaluation>> recommendEvaluations = new HashMap();

        all.forEach((user, ings) -> {
            List<RecommendEvaluation> recommendEvaluation = new ArrayList<>();
            int count = 0;
            for (int cw : ings.values()) {
                count += cw;
            }
            for (Ingredient ing : ings.keySet()) {
                RecommendEvaluation r = new RecommendEvaluation();
                r.setIngredient(ing);
                int  t = ings.get(ing);
                r.setScore((float) t / count);
                r.setMultiplicator(1);
                recommendEvaluation.add(r);
            }
            recommendEvaluations.put(user, recommendEvaluation);
        });

        ApplicationUser owner = userManager.getCurrentUser();
        var self = recommendEvaluations.get(owner);


        var bestguess = findMostSimilarUser(owner, self, recommendEvaluations);

        List<Recipe> mine = compareMap.get(owner);

        List<Recipe> recommends = new ArrayList<>();

        int current = 0;
        while (recommends.size() < 6) {
            var their = compareMap.get(bestguess.get(current));
            List<Recipe> result = their.stream()
              .filter(item -> !mine.contains(item))
              .toList();
            for (int i = 0; i < result.toArray().length; i++) {
                Recipe possible = result.get(i);
                if (!recommends.contains(possible)) {
                    recommends.add(possible);
                }
                if (recommends.size() >= 6) {
                    break;
                }
            }
            current++;
        }

        stopWatch.stop();
        var diff = stopWatch.getTotalTimeSeconds();
        return recommends;

    }



    public List<ApplicationUser> findMostSimilarUser(
      ApplicationUser targetUser,
      List<RecommendEvaluation> targetEvaluations,
      Map<ApplicationUser, List<RecommendEvaluation>> allUsersEvaluations) {

        List<Map.Entry<ApplicationUser, Double>> userDistances = new ArrayList<>();

        allUsersEvaluations.entrySet().parallelStream()
          .filter(entry -> !entry.getKey().equals(targetUser)) // Exclude the target user itself
          .forEach(entry -> {
              double distance = SimilarityUtils.calculateDistance(targetEvaluations, entry.getValue());
              userDistances.add(new AbstractMap.SimpleEntry<>(entry.getKey(), distance));
          });

        userDistances.sort(Comparator.comparingDouble(Map.Entry::getValue));

        List<ApplicationUser> sortedUsers = userDistances.stream()
          .map(Map.Entry::getKey)
          .collect(Collectors.toList());

        return sortedUsers;
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
            updateMapOfNutritions(recipeIngredient, nutritions);
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

    private void updateMapOfNutritions(RecipeIngredient recipeIngredient, Map<Nutrition, BigDecimal> nutritions) {
        LOGGER.trace("updateMapOfNutritions({}, {})",
            recipeIngredient, nutritions);

        for (IngredientNutrition nutrition : recipeIngredient.getIngredient().getNutritions()) {
            BigDecimal nutritionValue = nutrition.getValue()
                .multiply((recipeIngredient.getAmount())
                    .divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
            if (nutritions.containsKey(nutrition.getNutrition())) {
                nutritions.put(nutrition.getNutrition(), nutritionValue
                    .add(nutritions.get(nutrition.getNutrition())));
            } else {
                nutritions.put(nutrition.getNutrition(), nutritionValue);
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
