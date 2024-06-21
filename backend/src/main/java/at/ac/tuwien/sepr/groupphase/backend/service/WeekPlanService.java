package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface WeekPlanService {

    WeekPlanDetailDto[] getWeekplanDetail(Long id, Date from, Date to);


}
