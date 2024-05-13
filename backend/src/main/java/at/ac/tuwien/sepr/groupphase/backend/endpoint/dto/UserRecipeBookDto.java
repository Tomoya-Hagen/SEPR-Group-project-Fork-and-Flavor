package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

public record UserRecipeBookDto(

    @NotNull
    Long userId,

    @NotNull
    Long recipeBookId,

    @NotNull
    String permission
)
{
}
