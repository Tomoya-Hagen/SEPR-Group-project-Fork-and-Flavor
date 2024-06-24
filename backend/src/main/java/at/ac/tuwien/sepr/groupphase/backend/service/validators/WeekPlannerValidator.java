package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeekPlannerValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private RecipeBookRepository recipeBookRepository;

    public void validateForCreate(WeekPlanCreateDto weekPlanCreateDto) throws ValidationException {
        LOGGER.trace("validateForCreate({})", weekPlanCreateDto);
        List<String> validationErrors = new ArrayList<>();
        long x = ChronoUnit.DAYS.between(weekPlanCreateDto.startDate(), weekPlanCreateDto.endDate());
        if (x < 1) {
            validationErrors.add("The span of the week plan has to be greater than or equal to 1");
        }
        if (weekPlanCreateDto.startDate().isBefore(LocalDate.now())) {
            validationErrors.add("start date cannot be before today");
        }
        if (weekPlanCreateDto.endDate().isAfter(LocalDate.now().plusYears(1))) {
            validationErrors.add("end date has to be in the next year");
        }
        if (weekPlanCreateDto.weekdays().size() != 7) {
            validationErrors.add("every day has to be filled");
        }
        if (weekPlanCreateDto.startDate().isAfter(weekPlanCreateDto.endDate())) {
            validationErrors.add("start date has to be before the end date");
        }
        RecipeBook rb = recipeBookRepository.findById(weekPlanCreateDto.recipeBookId()).orElse(null);
        if (rb == null) {
            validationErrors.add("No recipebook found");
        } else {
            int breakfastCount = 0;
            int nonBreakfastCount = 0;
            for (Recipe recipe : rb.getRecipes()) {
                if (recipe.getCategories().contains("Frühstück")) {
                    breakfastCount++;
                } else if (!recipe.getCategories().contains("Dessert")
                    && !recipe.getCategories().contains("Beilage")
                    && !recipe.getCategories().contains("Vorspeise")
                    && !recipe.getCategories().contains("Jause")) {
                    nonBreakfastCount++;
                }
            }
            if (breakfastCount == 0) {
                validationErrors.add("Not enough breakfast recipes in the recipe book!");
            }
            if (nonBreakfastCount == 0) {
                validationErrors.add("Not enough lunch and dinner recipes in the recipe book!");
            }
        }
        if (!validationErrors.isEmpty()) {
            LOGGER.warn("Validation of Recipe to create failed for {}", validationErrors);
            throw new ValidationException("Validation of Recipe to create failed", validationErrors);
        }
    }
}
