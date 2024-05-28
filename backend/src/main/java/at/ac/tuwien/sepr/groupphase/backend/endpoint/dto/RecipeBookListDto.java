package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;
import jakarta.validation.constraints.NotNull;

/**
 * This is a record class representing a RecipeBookListDto.
 *
 * @param id The unique identifier of the RecipeBook.
 * @param name The name of the RecipeBook.
 * @param description A brief description of the RecipeBook.
 * @param ownerId The unique identifier of the owner of the RecipeBook.
 */
public record RecipeBookListDto(
    @NotNull
    long id,
    @NotNull
    String name,
    String description,
    long ownerId) {
}
