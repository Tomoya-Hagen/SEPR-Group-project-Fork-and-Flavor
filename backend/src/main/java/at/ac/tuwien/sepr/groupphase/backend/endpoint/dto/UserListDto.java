package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserListDto(
    @NotNull
    @Min(1)
    long id,
    @NotNull
    @Size(min = 1, max = 100)
    String name
) {
}
