package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The WeekPlanService interface provides methods for retrieving week plan details.
 */
@Service
public interface WeekPlanService {

    /**
     * Retrieves an array of WeekPlanDetailDto entities for a given id and date range.
     *
     * @param id The id of the week plan to retrieve details for.
     * @param from The start date of the range.
     * @param to The end date of the range.
     * @return An array of WeekPlanDetailDto entities.
     */
    WeekPlanDetailDto[] getWeekplanDetail(Long id, Date from, Date to);


}
