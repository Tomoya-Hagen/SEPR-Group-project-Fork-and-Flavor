package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

public record RecipeBookListDto(
    @NotNull
    long id,
    @NotNull
    String name,
    String description,
    long ownerId) {
}
