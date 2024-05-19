package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Dto for creating recipe books
 *
 * @param name
 * @param description
 * @param ownerId
 * @param users
 */
public record RecipeBookCreateDto (
    @NotNull
    @Size(min = 1, max = 100)
    String name,

    @NotNull
    String description,

    @NotNull
    Long ownerId,

    @NotNull
    List<UserListDto> users,

    @NotNull
    List<RecipeListDto> recipes

) {
}
