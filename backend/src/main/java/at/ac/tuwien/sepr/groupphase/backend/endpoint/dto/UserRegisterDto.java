package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.service.validators.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@ValidPassword
public record UserRegisterDto(
    @NotBlank(message = "Email must not be null")
    @Email(message = "Email must be a valid email address")
    @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters")
    String email,

    @NotBlank(message = "Password must not be null")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String password,

    @NotBlank(message = "Username must not be null")
    @Pattern(regexp = "\\A\\p{ASCII}*\\z", message = "Username must only contain ASCII characters")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
    String username
) {
}
