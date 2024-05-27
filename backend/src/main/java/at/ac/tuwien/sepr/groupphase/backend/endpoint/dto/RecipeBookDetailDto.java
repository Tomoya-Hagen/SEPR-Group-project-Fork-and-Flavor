package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;

public record RecipeBookDetailDto(
    long id,
    String name,
    String description,
    long ownerId,
    List<UserListDto> users,
    List<RecipeListDto> recipes) {
}
