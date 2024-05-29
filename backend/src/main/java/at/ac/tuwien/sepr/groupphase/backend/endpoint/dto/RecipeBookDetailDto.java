package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipeBookDetailDto(
    @NotNull
    long id,
    @NotNull
    String name,
    String description,
    long ownerId,
    List<UserListDto> users,
    List<RecipeListDto> recipes) {
}
