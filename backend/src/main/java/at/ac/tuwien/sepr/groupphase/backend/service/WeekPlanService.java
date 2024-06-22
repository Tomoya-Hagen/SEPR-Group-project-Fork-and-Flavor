package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface WeekPlanService {

    WeekPlanDetailDto[] getWeekplanDetail(Long id, Date from, Date to);

    /**
     * creates a new weekly planer for a given time and a given recipe book.
     *
     * @param weekPlanCreateDto contains the information to create a week planer.
     */
    WeekPlanDetailDto[] create(WeekPlanCreateDto weekPlanCreateDto) throws ValidationException;
}
