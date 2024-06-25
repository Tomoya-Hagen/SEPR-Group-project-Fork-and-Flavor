package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.service.DayTime;
import at.ac.tuwien.sepr.groupphase.backend.service.Weekday;

import java.util.List;

public record WeekDayDto(
    Weekday weekday,
    List<DayTime> dayTimes
){}
