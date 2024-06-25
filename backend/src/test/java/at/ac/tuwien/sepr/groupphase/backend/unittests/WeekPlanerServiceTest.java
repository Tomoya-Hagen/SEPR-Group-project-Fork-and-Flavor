package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekDayDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.DayTime;
import at.ac.tuwien.sepr.groupphase.backend.service.WeekPlanService;
import at.ac.tuwien.sepr.groupphase.backend.service.Weekday;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class WeekPlanerServiceTest implements TestData {
    @Autowired
    private WeekPlanService weekPlanService;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void createThrowsErrorIfWeekPlanForThisTimeSpanExists() throws ValidationException {
        userAuthenticationByEmail("admin@email.com");
        WeekPlanCreateDto firstPlan =
            new WeekPlanCreateDto(8L,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 5, 31),
                List.of(new WeekDayDto(Weekday.Monday, List.of(DayTime.Breakfast)),
                    new WeekDayDto(Weekday.Tuesday, List.of()),
                    new WeekDayDto(Weekday.Wednesday, List.of()),
                    new WeekDayDto(Weekday.Thursday, List.of()),
                    new WeekDayDto(Weekday.Friday, List.of()),
                    new WeekDayDto(Weekday.Saturday, List.of()),
                    new WeekDayDto(Weekday.Sunday, List.of())));
        WeekPlanDetailDto[] weekPlanerResponse = weekPlanService.create(firstPlan);
        WeekPlanCreateDto secondPlan =
            new WeekPlanCreateDto(8L,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 5, 31),
                List.of(new WeekDayDto(Weekday.Monday, List.of(DayTime.Breakfast)),
                    new WeekDayDto(Weekday.Tuesday, List.of()),
                    new WeekDayDto(Weekday.Wednesday, List.of()),
                    new WeekDayDto(Weekday.Thursday, List.of()),
                    new WeekDayDto(Weekday.Friday, List.of()),
                    new WeekDayDto(Weekday.Saturday, List.of()),
                    new WeekDayDto(Weekday.Sunday, List.of())));
        Assertions.assertThrows(DuplicateObjectException.class, () -> weekPlanService.create(secondPlan));
    }

    @Test
    void weekPlannerSuccessfullyCreatedContainsSevenBreakfastsAndFourteenMainDishesAndStartAndEndDatesAreCorrect() throws ValidationException {
        userAuthenticationByEmail("admin@email.com");
        WeekPlanCreateDto createThis = new WeekPlanCreateDto(
            8L,
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 7),
            List.of(new WeekDayDto(Weekday.Monday, List.of(DayTime.Breakfast, DayTime.Lunch, DayTime.Dinner)),
                new WeekDayDto(Weekday.Tuesday, List.of(DayTime.Breakfast, DayTime.Lunch, DayTime.Dinner)),
                new WeekDayDto(Weekday.Wednesday, List.of(DayTime.Breakfast, DayTime.Lunch, DayTime.Dinner)),
                new WeekDayDto(Weekday.Thursday, List.of(DayTime.Breakfast, DayTime.Lunch, DayTime.Dinner)),
                new WeekDayDto(Weekday.Friday, List.of(DayTime.Breakfast, DayTime.Lunch, DayTime.Dinner)),
                new WeekDayDto(Weekday.Saturday, List.of(DayTime.Breakfast, DayTime.Lunch, DayTime.Dinner)),
                new WeekDayDto(Weekday.Sunday, List.of(DayTime.Breakfast, DayTime.Lunch, DayTime.Dinner))));

        WeekPlanDetailDto[] weekPlanerResponse = weekPlanService.create(createThis);

        int breakfastCount = 0;
        int lunchOrDinnerCount = 0;
        Category breakfastCat = categoryRepository.findByName("Frühstück").getFirst();
        Category lunchOrDinner = categoryRepository.findByName("Hauptspeise").getFirst();
        Category vorsp = categoryRepository.findByName("Vorspeise").getFirst();
        Category dessert = categoryRepository.findByName("Dessert").getFirst();
        Category jause = categoryRepository.findByName("Jause").getFirst();
        Category beilage = categoryRepository.findByName("Beilage").getFirst();
        for (WeekPlanDetailDto dto : weekPlanerResponse) {
            Recipe lunch = recipeRepository.findRecipeByName(dto.lunch.getRecipename()).get();
            Recipe breakfast = recipeRepository.findRecipeByName(dto.breakfast.getRecipename()).get();
            Recipe dinner = recipeRepository.findRecipeByName(dto.dinner.getRecipename()).get();
            if (breakfast.getCategories().contains(breakfastCat)) {
                breakfastCount++;
            }
            if (lunch.getCategories().contains(lunchOrDinner)
                    && !lunch.getCategories().contains(vorsp)
                    && !lunch.getCategories().contains(dessert)
                    && !lunch.getCategories().contains(beilage)
                    && !lunch.getCategories().contains(jause)
                    && !lunch.getCategories().contains(breakfastCat)) {
                lunchOrDinnerCount++;
            }
            if (dinner.getCategories().contains(lunchOrDinner)
                    && !dinner.getCategories().contains(vorsp)
                    && !dinner.getCategories().contains(dessert)
                    && !dinner.getCategories().contains(beilage)
                    && !dinner.getCategories().contains(jause)
                    && !dinner.getCategories().contains(breakfastCat)) {
                lunchOrDinnerCount++;
            }
        }
        Assertions.assertEquals(7, breakfastCount);
        Assertions.assertEquals(14, lunchOrDinnerCount);
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 7);
        LocalDate testStart = new java.sql.Date(weekPlanerResponse[0].date.getTime()).toLocalDate();
        LocalDate testEnd = new java.sql.Date(weekPlanerResponse[weekPlanerResponse.length - 1].date.getTime()).toLocalDate();
        Assertions.assertEquals(startDate, testStart);
        Assertions.assertEquals(endDate, testEnd);
    }
}
