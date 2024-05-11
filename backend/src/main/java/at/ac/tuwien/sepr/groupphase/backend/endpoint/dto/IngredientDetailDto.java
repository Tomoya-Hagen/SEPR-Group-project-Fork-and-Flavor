package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.math.BigDecimal;

public record IngredientDetailDto(
    long id,
    String name,
    BigDecimal amount,
    long unit
) {
}
