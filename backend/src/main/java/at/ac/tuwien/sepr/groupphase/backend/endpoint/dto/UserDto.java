package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto(
    @NotNull
    long id,
    @NotNull
    @Size(min = 1, max = 100)
    String name
) {
}
