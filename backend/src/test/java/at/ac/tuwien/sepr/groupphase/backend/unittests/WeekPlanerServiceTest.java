package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekDayDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
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
    void weekPlannerSuccessfulyyCreatedContainsTwentyOneRecipes() throws ValidationException {
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

        int count = 0;
        for (WeekPlanDetailDto dto : weekPlanerResponse) {
            if (dto.breakfast.getRecipeId() > 0) {
                count++;
            }
            if (dto.lunch.getRecipeId() > 0) {
                count++;
            }
            if (dto.dinner.getRecipeId() > 0) {
                count++;
            }
        }
        Assertions.assertEquals(21, count);

    }
}
