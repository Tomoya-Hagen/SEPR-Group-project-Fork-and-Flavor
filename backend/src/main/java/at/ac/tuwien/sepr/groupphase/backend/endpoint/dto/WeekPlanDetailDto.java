package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.Date;

public class WeekPlanDetailDto {

    public Date date;
    public SimpleRecipeResultDto breakfast;
    public SimpleRecipeResultDto lunch;
    public SimpleRecipeResultDto dinner;

    public WeekPlanDetailDto(Date date, SimpleRecipeResultDto breakfast, SimpleRecipeResultDto lunch, SimpleRecipeResultDto dinner) {
        this.date = date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public WeekPlanDetailDto() {
    }
}
