package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.math.BigDecimal;

public record NutritionDetailDto(
    long id,
    String name,
    String description,
    String unit,
    BigDecimal value
) {
}
