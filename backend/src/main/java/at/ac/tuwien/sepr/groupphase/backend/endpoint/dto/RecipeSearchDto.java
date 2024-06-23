package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecipeSearchDto(

    @NotNull
    @Size(min = 1, max = 100)
    String name,

    @NotNull
    Long categorieId
) {
}
