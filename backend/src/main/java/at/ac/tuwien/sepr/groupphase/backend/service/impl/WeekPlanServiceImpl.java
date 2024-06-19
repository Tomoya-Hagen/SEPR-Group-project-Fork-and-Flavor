package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.WeeklyPlannerMapper;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.WeeklyPlannerRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.WeekPlanService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Transactional
@Service
public class WeekPlanServiceImpl implements WeekPlanService {


    private final WeeklyPlannerMapper weeklyPlannerMapper;
    private final WeeklyPlannerRepository weeklyPlannerRepository;


    public WeekPlanServiceImpl(WeeklyPlannerRepository weeklyPlannerRepository, WeeklyPlannerMapper weeklyPlannerMapper) {
        this.weeklyPlannerRepository = weeklyPlannerRepository;
        this.weeklyPlannerMapper = weeklyPlannerMapper;
    }

    @Override
    public WeekPlanDetailDto[] getWeekplanDetail(Long id, Date from, Date to) {
        var dfrom = new java.sql.Date(from.getTime());
        var dto = new java.sql.Date(to.getTime());
        var result = weeklyPlannerRepository.findWeeklyPlannerByDate(id, dfrom, dto);

        return weeklyPlannerMapper.weeklyPlannerArrtoWeekPlanDetailDtoArr(result);
    }
}
