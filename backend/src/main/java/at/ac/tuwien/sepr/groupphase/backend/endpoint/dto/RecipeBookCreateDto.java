package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Dto for creating recipe books.
 *
 * @param name the name of the recipe to be created
 * @param description the corresponding description
 * @param users users that can interact with the recipe book
 */
public record RecipeBookCreateDto(
    @NotNull
    @Size(min = 1, max = 100)
    String name,

    @NotNull
    @Size(min = 1, max = 300) // adjust later
    String description,

    @NotNull
    List<UserListDto> users,

    @NotNull
    List<RecipeListDto> recipes

) {
}
