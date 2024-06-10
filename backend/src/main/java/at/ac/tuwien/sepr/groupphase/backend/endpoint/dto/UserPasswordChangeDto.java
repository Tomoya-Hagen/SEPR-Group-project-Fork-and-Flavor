package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordChangeDto(
    @NotBlank(message = "Password must not be null")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String oldPassword,

    @NotBlank(message = "Password must not be null")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String newPassword
) {

}
