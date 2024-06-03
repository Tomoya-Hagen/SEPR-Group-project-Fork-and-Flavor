package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Size(max = 3000)
    String description,

    List<UserListDto> users,

    @NotNull
    @Size(min = 1)
    List<RecipeListDto> recipes

) {
}
