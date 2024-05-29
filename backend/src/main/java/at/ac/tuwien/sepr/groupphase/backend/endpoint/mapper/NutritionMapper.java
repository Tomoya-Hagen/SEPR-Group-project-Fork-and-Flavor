package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NutritionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This mapper converts a nutrition into a nutrition dto.
 */
@Mapper
public interface NutritionMapper {
    /**
     * This method creates a NutritionDetailDto out of a nutrition entity and a value.
     *
     * @param nutrition represents a nutrition that appears in a recipe.
     * @param value     represents the amount of the nutrition in the recipe.
     * @return a combined dto from the given objects.
     */
    NutritionDetailDto nutritionAndValueToNutritionDetailDto(Nutrition nutrition, BigDecimal value);

    /**
     * This method maps a hashmap to a list of NutritionDetailDto.
     *
     * @param nutritionBigDecimalHashMap the hashmap contains the Nutrition entity as key and
     *                                   the amount for this nutrition as value.
     * @return the mapped list of NutritionDetailDto
     */
    default ArrayList<NutritionDetailDto> nutritionAndValueHashmapToNutritionDetailDto(HashMap<Nutrition, BigDecimal> nutritionBigDecimalHashMap) {
        ArrayList<NutritionDetailDto> result = new ArrayList<>();
        for (Nutrition nutrition : nutritionBigDecimalHashMap.keySet()) {
            result.add(nutritionAndValueToNutritionDetailDto(nutrition, nutritionBigDecimalHashMap.get(nutrition)));
        }
        return result;
    }
}
