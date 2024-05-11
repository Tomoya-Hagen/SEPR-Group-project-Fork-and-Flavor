package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NutritionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface NutritionMapper {
    NutritionDetailDto nutritionAndValueToNutritionDetailDto(Nutrition nutrition, BigDecimal value);
}
