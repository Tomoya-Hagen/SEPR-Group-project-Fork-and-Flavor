// src/main/java/at/ac/tuwien/sepr/groupphase/backend/endpoint/dto/UserRegisterDto.java

package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRegisterDto(
    @NotNull(message = "Email must not be null") @Email String email,
    @NotNull(message = "Password must not be null") String password,
    @NotNull(message = "Username must not be null") String username
) {
}
