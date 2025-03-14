package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipeBookUpdateDto(
    @NotNull
    long id,
    @NotNull
    String name,
    String description,
    long ownerId,
    UserDto owner,
    List<UserListDto> users,
    List<RecipeListDto> recipes
) {
}
