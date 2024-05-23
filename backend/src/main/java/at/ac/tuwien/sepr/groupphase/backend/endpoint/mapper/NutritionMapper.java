package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NutritionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the interface for the Nutrition entities and related DTOs.
 *
 */
@Mapper
public interface NutritionMapper {
    NutritionDetailDto nutritionAndValueToNutritionDetailDto(Nutrition nutrition, BigDecimal value);

    default ArrayList<NutritionDetailDto> nutritionAndValueHashmapToNutritionDetailDto(HashMap<Nutrition, BigDecimal> nutritionBigDecimalHashMap) {
        ArrayList<NutritionDetailDto> result = new ArrayList<>();
        for (Nutrition nutrition : nutritionBigDecimalHashMap.keySet()) {
            result.add(nutritionAndValueToNutritionDetailDto(nutrition, nutritionBigDecimalHashMap.get(nutrition)));
        }
        return result;
    }
}
