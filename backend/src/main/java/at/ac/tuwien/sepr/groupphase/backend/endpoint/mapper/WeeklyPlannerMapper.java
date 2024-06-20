package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.WeeklyPlanner;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class WeeklyPlannerMapper {


    @Autowired
    private RecipeMapper recipeMapper;



    public WeekPlanDetailDto[] weeklyPlannerArrtoWeekPlanDetailDtoArr(WeeklyPlanner[] planner) {
        List<WeekPlanDetailDto> result = new ArrayList<>();
        WeekPlanDetailDto dto = new WeekPlanDetailDto();
        for (WeeklyPlanner plan : planner) {
            if (dto.date == null || !dto.date.equals(plan.getDate())) {
                if (dto.date != null) {
                    result.add(dto);
                }
                dto = new WeekPlanDetailDto();
                dto.date = plan.getDate();
            }
            switch (plan.getDaytime()) {
                case Frühstück -> {
                    dto.breakfast = this.recipeMapper.recipeToRecipeResultDto(plan.getRecipe());
                }
                case Abendessen -> {
                    dto.dinner = this.recipeMapper.recipeToRecipeResultDto(plan.getRecipe());
                }
                case Mittagessen -> {
                    dto.lunch = this.recipeMapper.recipeToRecipeResultDto(plan.getRecipe());
                }
                default -> throw new IllegalStateException("Unexpected value: " + plan.getDaytime());
            }
        }
        if (dto.date != null) {
            result.add(dto);
        }
        WeekPlanDetailDto[] dtoArr = new WeekPlanDetailDto[result.size()];
        result.toArray(dtoArr);
        return dtoArr;
    }
}
