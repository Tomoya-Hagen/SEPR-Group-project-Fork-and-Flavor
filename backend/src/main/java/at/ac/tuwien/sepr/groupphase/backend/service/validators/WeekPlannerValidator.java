package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeekPlannerValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void validateForCreate(WeekPlanCreateDto weekPlanCreateDto) throws ValidationException {
        LOGGER.trace("validateForCreate({})", weekPlanCreateDto);
        List<String> validationErrors = new ArrayList<>();
        if (weekPlanCreateDto.startDate().isBefore(LocalDate.now())) {
            validationErrors.add("start date cannot be before today");
        }
        if (weekPlanCreateDto.endDate().isAfter(weekPlanCreateDto.endDate().plusYears(1))) {
            validationErrors.add("end date has to be in the next year");
        }
        if (weekPlanCreateDto.weekdays().size() != 7) {
            validationErrors.add("every day has to be filled");
        }
        if (weekPlanCreateDto.startDate().isAfter(weekPlanCreateDto.endDate())) {
            validationErrors.add("start date has to be before the end date");
        }
        if (!validationErrors.isEmpty()) {
            LOGGER.warn("Validation of Recipe to create failed for {}", validationErrors);
            throw new ValidationException("Validation of Recipe to create failed", validationErrors);
        }
    }
}
