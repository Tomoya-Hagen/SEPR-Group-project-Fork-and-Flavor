package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Dto for creating recipe books
 *
 * @param id
 * @param name
 * @param description
 * @param ownerId
 * @param userRecipeBooks
 */
public record RecipeBookCreateDto (
    @NotNull
    Long id,

    @NotNull
    @Size(min = 1, max = 100)
    String name,

    @NotNull
    String description,

    @NotNull
    Long ownerId,

    @NotNull
    List<UserRecipeBookDto> userRecipeBooks,

    @NotNull
    List<RecipeListDto> recipes

) {
}
