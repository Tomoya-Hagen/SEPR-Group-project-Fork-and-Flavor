package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecipeListDto(
    @NotNull
    long id,

    @NotNull
    @Size(min = 1, max = 100)
    String name,

    @NotNull
    String description,

    @NotNull
    long rating
) {
}