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
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class WeekPlanServiceImpl implements WeekPlanService {


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
        calculateWeekPlan(weeklyPlannerItems, recipesWithNutrition);
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
                            .divide(BigDecimal.valueOf(recipe.getNumberOfServings()), RoundingMode.UNNECESSARY))
                            .divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY));
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
        Map<Nutrition, Double> recommendedNutrition = getDailyRecommendedNutritions();
        List<List<WeeklyPlanner>> currentWeekPlan = List.copyOf(weeklyPlannerItems);
        List<List<WeeklyPlanner>> currentBestWeekPlan = List.copyOf(weeklyPlannerItems);
        double currentBestAttemptRate = Double.MAX_VALUE;
        int calculationAttempt = 0;
        List<Recipe> usedBreakfastRecipes = new ArrayList<>();
        List<Recipe> usedNonBreakfastRecipes = new ArrayList<>();
        List<Recipe> breakfastRecipes = recipes.keySet().stream().filter(r -> r.getCategories().contains(categoryRepository.findFirstByName("Frühstück"))).toList();
        List<Recipe> nonBreakfastRecipes = recipes.keySet().stream().filter(r -> !r.getCategories().contains(categoryRepository.findFirstByName("Frühstück"))).toList();
        while (calculationAttempt < 10) {
            for (int i = 0; i < weeklyPlannerItems.size(); i++) {
                Map<String, Double> nutritions = new HashMap<>();
                List<WeeklyPlanner> currentDay = weeklyPlannerItems.get(i);
                for (int j = 0; j < currentDay.size(); j++) {
                }
            }
            int currentAttemptRate = 0;
            if (currentBestAttemptRate > currentAttemptRate) {
                calculationAttempt = 0;
                currentBestWeekPlan = currentWeekPlan;
            } else {
                calculationAttempt++;
            }
        }
        for (List<WeeklyPlanner> weeklyPlannerItem : currentBestWeekPlan) {
            weeklyPlannerRepository.saveAll(weeklyPlannerItem);
        }
    }

    private Map<Nutrition, Double> getDailyRecommendedNutritions() {
        Map<Nutrition, Double> recommendedNutrition = new HashMap<>();
        recommendedNutrition.put(nutritionRepository.findByName("Kalorien").orElseThrow(NotFoundException::new), 2000d);
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
}
