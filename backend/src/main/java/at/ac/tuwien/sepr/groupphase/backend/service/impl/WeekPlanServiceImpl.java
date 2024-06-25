package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekDayDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.WeeklyPlannerMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.IngredientNutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.WeeklyPlanner;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.NutritionRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.WeeklyPlannerRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.DayTime;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
import at.ac.tuwien.sepr.groupphase.backend.service.WeekPlanService;
import at.ac.tuwien.sepr.groupphase.backend.service.Weekday;
import at.ac.tuwien.sepr.groupphase.backend.service.validators.WeekPlannerValidator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional
@Service
public class WeekPlanServiceImpl implements WeekPlanService {

    private List<ApplicationUser> currentCreators = new ArrayList<>();
    private final Object lock = new Object();
    private final WeeklyPlannerMapper weeklyPlannerMapper;
    private final WeeklyPlannerRepository weeklyPlannerRepository;
    private final RecipeBookRepository recipeBookRepository;
    private final UserManager userManager;
    private final WeekPlannerValidator weekPlannerValidator;
    private final CategoryRepository categoryRepository;
    private final NutritionRepository nutritionRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public WeekPlanServiceImpl(WeeklyPlannerRepository weeklyPlannerRepository,
                               WeeklyPlannerMapper weeklyPlannerMapper,
                               RecipeBookRepository recipeBookRepository,
                               UserManager userManager,
                               WeekPlannerValidator weekPlannerValidator,
                               CategoryRepository categoryRepository,
                               NutritionRepository nutritionRepository) {
        this.weeklyPlannerRepository = weeklyPlannerRepository;
        this.weeklyPlannerMapper = weeklyPlannerMapper;
        this.recipeBookRepository = recipeBookRepository;
        this.userManager = userManager;
        this.weekPlannerValidator = weekPlannerValidator;
        this.categoryRepository = categoryRepository;
        this.nutritionRepository = nutritionRepository;
    }

    @Override
    public WeekPlanDetailDto[] getWeekplanDetail(Long id, Date from, Date to) {
        var dfrom = new java.sql.Date(from.getTime());
        var dto = new java.sql.Date(to.getTime());
        var result = weeklyPlannerRepository.findWeeklyPlannerByDate(id, dfrom, dto);

        return weeklyPlannerMapper.weeklyPlannerArrtoWeekPlanDetailDtoArr(result);
    }

    @Override
    public WeekPlanDetailDto[] getextendedWeekplanDetail(Long id, Date from, int limit) {
        var dfrom = new java.sql.Date(from.getTime());
        PageRequest pageRequest = PageRequest.of(0, limit);
        List<java.sql.Date> filledDates = weeklyPlannerRepository.findNextFilledDates(id, dfrom, pageRequest);
        var result = weeklyPlannerRepository.findByDates(id, filledDates);

        return weeklyPlannerMapper.weeklyPlannerArrtoWeekPlanDetailDtoArr(result);
    }

    @Override
    public WeekPlanDetailDto[] create(WeekPlanCreateDto weekPlanCreateDto) throws ValidationException, ForbiddenException, DuplicateObjectException, NotFoundException {
        LOGGER.trace("create({})", weekPlanCreateDto);
        ApplicationUser user = userManager.getCurrentUser();
        RecipeBook recipeBook = recipeBookRepository.findById(weekPlanCreateDto.recipeBookId()).orElseThrow(NotFoundException::new);
        if (!user.equals(recipeBook.getOwner()) && !recipeBook.getEditors().contains(user)) {
            throw new ForbiddenException();
        }
        if (!weeklyPlannerRepository.getWeeklyPlannerItemThatIsInTheGivenTimeFromTheGivenRecipeBook(
                weekPlanCreateDto.recipeBookId(),
                weekPlanCreateDto.startDate(),
                weekPlanCreateDto.endDate()).isEmpty()) {
            throw new DuplicateObjectException("week planer for this timespan already exists");
        }
        weekPlannerValidator.validateForCreate(weekPlanCreateDto);
        List<List<WeeklyPlanner>> weeklyPlannerItems = new ArrayList<>();
        LocalDate date = weekPlanCreateDto.startDate();
        while (!date.isAfter(weekPlanCreateDto.endDate())) {
            List<WeeklyPlanner> day = new ArrayList<>();
            LocalDate currentDate = date;
            addWeekPlanItemsToListBasedOnWeekDay(
                    weekPlanCreateDto.weekdays().stream().filter(w -> w.weekday().equals(Weekday.values()[currentDate.getDayOfWeek().ordinal()])).findFirst()
                            .orElseThrow(NotFoundException::new),
                    day,
                    recipeBook,
                    date);
            weeklyPlannerItems.add(day);
            date = date.plusDays(1);
        }
        Map<Recipe, Map<Nutrition, BigDecimal>> recipesWithNutrition = calculateNutritionForRecipes(recipeBook.getRecipes());
        synchronized (lock) {
            if (currentCreators.contains(user)) {
                throw new ForbiddenException("You are currently creating a weekplan");
            }
            currentCreators.add(user);
        }
        calculateWeekPlan(weeklyPlannerItems, recipesWithNutrition);
        synchronized (lock) {
            currentCreators.remove(user);
        }
        return getWeekplanDetail(weekPlanCreateDto.recipeBookId(),
                java.sql.Date.valueOf(weekPlanCreateDto.startDate()),
                java.sql.Date.valueOf(weekPlanCreateDto.endDate()));
    }

    private Map<Recipe, Map<Nutrition, BigDecimal>> calculateNutritionForRecipes(List<Recipe> recipes) {
        Map<Recipe, Map<Nutrition, BigDecimal>> recipeWithNutrition = new HashMap<>();
        for (Recipe recipe : recipes) {
            Map<Nutrition, BigDecimal> nutritionPerRecipe = new HashMap<>();
            List<RecipeIngredient> recipeIngredients = recipe.getIngredients();
            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                for (IngredientNutrition nutrition : recipeIngredient.getIngredient().getNutritions()) {
                    BigDecimal nutritionValue = nutrition.getValue()
                            .multiply((recipeIngredient.getAmount()
                                    .divide(BigDecimal.valueOf(recipe.getNumberOfServings()), RoundingMode.DOWN))
                                    .divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
                    if (nutritionPerRecipe.containsKey(nutrition.getNutrition())) {
                        nutritionPerRecipe.put(nutrition.getNutrition(), nutritionValue
                                .add(nutritionPerRecipe.get(nutrition.getNutrition())));
                    } else {
                        nutritionPerRecipe.put(nutrition.getNutrition(), nutritionValue);
                    }
                }
            }
            recipeWithNutrition.put(recipe, nutritionPerRecipe);
        }
        return recipeWithNutrition;
    }

    private void calculateWeekPlan(List<List<WeeklyPlanner>> weeklyPlannerItems, Map<Recipe, Map<Nutrition, BigDecimal>> recipes) {
        Map<Nutrition, Double> recommendedNutritionMin = getDailyRecommendedNutritionsMin();
        Map<Nutrition, Double> recommendedNutritionMax = getDailyRecommendedNutritionsMax();
        List<List<WeeklyPlanner>> bestWeekPlan = new ArrayList<>();
        double bestAttemptRate = Double.MAX_VALUE;
        Map<Recipe, Map<Nutrition, BigDecimal>> nutritionPerRecipe = calculateNutritionForRecipes(new ArrayList<>(recipes.keySet()));
        Set<Recipe> usedBreakfastRecipes = new HashSet<>();
        Set<Recipe> usedLunchOrDinnerRecipes = new HashSet<>();

        for (int attempt = 0; attempt < 10; attempt++) {
            List<List<WeeklyPlanner>> currentWeekPlan = copyWeekPlan(weeklyPlannerItems);

            List<Recipe> breakfastRecipes = recipes.keySet().stream()
                    .filter(r -> r.getCategories().contains(categoryRepository.findFirstByName("Frühstück")))
                    .toList();
            List<Recipe> lunchOrDinnerRecipes = recipes.keySet().stream()
                    .filter(r -> {
                        var categories = r.getCategories();
                        return categories.contains(categoryRepository.findFirstByName("Hauptspeise"))
                                && !categories.contains(categoryRepository.findFirstByName("Frühstück"))
                                && !categories.contains(categoryRepository.findFirstByName("Dessert"))
                                && !categories.contains(categoryRepository.findFirstByName("Jause"))
                                && !categories.contains(categoryRepository.findFirstByName("Vorspeise"))
                                && !categories.contains(categoryRepository.findFirstByName("Beilage"));
                    }).toList();

            double totalAttemptRate = 0.0;

            for (List<WeeklyPlanner> dayPlan : currentWeekPlan) {
                Map<Nutrition, BigDecimal> dailyNutrition = new HashMap<>();
                for (WeeklyPlanner planner : dayPlan) {
                    Recipe selectedRecipe;
                    if (planner.getDaytime().equals(WeeklyPlanner.EatingTime.Frühstück)) {
                        selectedRecipe = getBestRecipe(breakfastRecipes, dailyNutrition, recommendedNutritionMin, recommendedNutritionMax, nutritionPerRecipe, usedBreakfastRecipes);
                        usedBreakfastRecipes.add(selectedRecipe);
                        if (usedBreakfastRecipes.size() == breakfastRecipes.size()) {
                            usedBreakfastRecipes.clear();
                        }
                    } else {
                        selectedRecipe = getBestRecipe(lunchOrDinnerRecipes, dailyNutrition, recommendedNutritionMin, recommendedNutritionMax, nutritionPerRecipe, usedLunchOrDinnerRecipes);
                        usedLunchOrDinnerRecipes.add(selectedRecipe);
                        if (usedLunchOrDinnerRecipes.size() == lunchOrDinnerRecipes.size()) {
                            usedLunchOrDinnerRecipes.clear();
                        }
                    }
                    planner.setRecipe(selectedRecipe);

                    // Add the recipe's nutrition to the daily total
                    for (Map.Entry<Nutrition, BigDecimal> entry : recipes.get(selectedRecipe).entrySet()) {
                        dailyNutrition.merge(entry.getKey(), entry.getValue(), BigDecimal::add);
                    }
                }
                double attemptRate = calculateAttemptRate(dailyNutrition, recommendedNutritionMin, recommendedNutritionMax);
                totalAttemptRate += attemptRate;
            }
            if (totalAttemptRate < bestAttemptRate) {
                bestAttemptRate = totalAttemptRate;
                bestWeekPlan = copyWeekPlan(currentWeekPlan);
            }
        }
        for (List<WeeklyPlanner> weeklyPlannerItem : bestWeekPlan) {
            weeklyPlannerRepository.saveAll(weeklyPlannerItem);
        }
    }

    private Recipe getBestRecipe(List<Recipe> recipes, Map<Nutrition, BigDecimal> dailyNutrition, Map<Nutrition, Double> recommendedMin, Map<Nutrition, Double> recommendedMax, Map<Recipe,
            Map<Nutrition, BigDecimal>> nutritionPerRecipe, Set<Recipe> usedRecipes) {
        Recipe bestRecipe = null;
        double bestScore = Double.MAX_VALUE;

        List<Recipe> availableRecipes = recipes.stream().filter(r -> !usedRecipes.contains(r)).toList();

        if (availableRecipes.isEmpty()) {
            availableRecipes = recipes;
        }

        for (Recipe recipe : availableRecipes) {
            double score = 0.0;
            for (Map.Entry<Nutrition, BigDecimal> entry : nutritionPerRecipe.get(recipe).entrySet()) {
                Nutrition nutrition = entry.getKey();
                BigDecimal value = entry.getValue().add(dailyNutrition.getOrDefault(nutrition, BigDecimal.ZERO));
                double min = recommendedMin.getOrDefault(nutrition, 0.0);
                double max = recommendedMax.getOrDefault(nutrition, Double.MAX_VALUE);

                if (value.doubleValue() < min) {
                    score += min - value.doubleValue();
                } else if (value.doubleValue() > max) {
                    score += value.doubleValue() - max;
                }
            }

            if (score < bestScore) {
                bestScore = score;
                bestRecipe = recipe;
            }
        }

        return bestRecipe;
    }


    private Map<Nutrition, Double> getDailyRecommendedNutritionsMin() {
        Map<Nutrition, Double> recommendedNutrition = new HashMap<>();
        recommendedNutrition.put(nutritionRepository.findByName("Kalorien").orElseThrow(NotFoundException::new), 1900d);
        recommendedNutrition.put(nutritionRepository.findByName("Cholesterin").orElseThrow(NotFoundException::new), 1200d);
        recommendedNutrition.put(nutritionRepository.findByName("Ballaststoffe").orElseThrow(NotFoundException::new), 20000d);
        recommendedNutrition.put(nutritionRepository.findByName("Kohlenhydrate total").orElseThrow(NotFoundException::new), 200000d);
        recommendedNutrition.put(nutritionRepository.findByName("Salz").orElseThrow(NotFoundException::new), 1000d);
        recommendedNutrition.put(nutritionRepository.findByName("Fett total").orElseThrow(NotFoundException::new), 10000d);
        recommendedNutrition.put(nutritionRepository.findByName("Eiweiß").orElseThrow(NotFoundException::new), 55000d);

        return recommendedNutrition;
    }

    private Map<Nutrition, Double> getDailyRecommendedNutritionsMax() {
        Map<Nutrition, Double> recommendedNutrition = new HashMap<>();
        recommendedNutrition.put(nutritionRepository.findByName("Kalorien").orElseThrow(NotFoundException::new), 2500d);
        recommendedNutrition.put(nutritionRepository.findByName("Cholesterin").orElseThrow(NotFoundException::new), 3000d);
        recommendedNutrition.put(nutritionRepository.findByName("Ballaststoffe").orElseThrow(NotFoundException::new), 60000d);
        recommendedNutrition.put(nutritionRepository.findByName("Kohlenhydrate total").orElseThrow(NotFoundException::new), 1000000d);
        recommendedNutrition.put(nutritionRepository.findByName("Salz").orElseThrow(NotFoundException::new), 6000d);
        recommendedNutrition.put(nutritionRepository.findByName("Fett total").orElseThrow(NotFoundException::new), 80000d);
        recommendedNutrition.put(nutritionRepository.findByName("Eiweiß").orElseThrow(NotFoundException::new), 120000d);

        return recommendedNutrition;
    }

    private void addWeekPlanItemsToListBasedOnWeekDay(WeekDayDto weekDayDto, List<WeeklyPlanner> weeklyPlannerItems,
                                                      RecipeBook recipeBook, LocalDate date) {
        for (int i = 0; i < weekDayDto.dayTimes().size(); i++) {
            WeeklyPlanner weeklyPlanner = new WeeklyPlanner();
            weeklyPlanner.setRecipeBook(recipeBook);
            weeklyPlanner.setDate(java.sql.Date.valueOf(date));
            weeklyPlanner.setDaytime(WeeklyPlanner.EatingTime.values()
                    [DayTime.valueOf(weekDayDto.dayTimes().get(i).name()).ordinal()]);
            weeklyPlannerItems.add(weeklyPlanner);
        }
    }

    private double calculateAttemptRate(Map<Nutrition, BigDecimal> dailyNutrition, Map<Nutrition, Double> recommendedMin, Map<Nutrition, Double> recommendedMax) {
        double attemptRate = 0.0;
        for (Map.Entry<Nutrition, BigDecimal> entry : dailyNutrition.entrySet()) {
            Nutrition nutrition = entry.getKey();
            BigDecimal value = entry.getValue();
            double min = recommendedMin.getOrDefault(nutrition, 0.0);
            double max = recommendedMax.getOrDefault(nutrition, Double.MAX_VALUE);

            if (value.doubleValue() < min) {
                attemptRate += min - value.doubleValue();
            } else if (value.doubleValue() > max) {
                attemptRate += value.doubleValue() - max;
            }
        }
        return attemptRate;
    }

    private List<List<WeeklyPlanner>> copyWeekPlan(List<List<WeeklyPlanner>> weekPlan) {
        List<List<WeeklyPlanner>> copiedPlan = new ArrayList<>();
        for (List<WeeklyPlanner> dayPlan : weekPlan) {
            List<WeeklyPlanner> copiedDay = new ArrayList<>();
            for (WeeklyPlanner planner : dayPlan) {
                WeeklyPlanner copiedPlanner = new WeeklyPlanner();
                if (planner.getRecipe() != null) {
                    copiedPlanner.setRecipe(planner.getRecipe());
                }
                copiedPlanner.setRecipeBook(planner.getRecipeBook());
                copiedPlanner.setDate(planner.getDate());
                copiedPlanner.setDaytime(planner.getDaytime());
                copiedDay.add(copiedPlanner);
            }
            copiedPlan.add(copiedDay);
        }
        return copiedPlan;
    }
}
