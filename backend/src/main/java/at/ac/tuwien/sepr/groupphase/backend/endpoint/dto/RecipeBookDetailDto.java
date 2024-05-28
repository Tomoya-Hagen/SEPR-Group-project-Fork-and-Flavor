package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * This is a record class representing the detailed information of a RecipeBook.
 *
 * @param id The unique identifier of the RecipeBook.
 * @param name The name of the RecipeBook.
 * @param description A brief description of the RecipeBook.
 * @param ownerId The unique identifier of the owner of the RecipeBook.
 * @param recipes A list of RecipeDetailDto objects representing the recipes contained in the RecipeBook.
 */
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
