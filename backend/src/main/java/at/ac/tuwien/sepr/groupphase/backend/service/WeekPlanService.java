package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * This is the interface for the weekPlanService.
 */
@Service
public interface WeekPlanService {

    /**
     * gets a Array of WeekPlanDetailDto based on the given id, date from and date to.
     *
     * @param id represents the id.
     * @param from get date from.
     * @param to get date to.
     * @return a Array of WeekPlanDetailDto if a weekPlan with the given id exists.
     */
    WeekPlanDetailDto[] getWeekplanDetail(Long id, Date from, Date to);
}
