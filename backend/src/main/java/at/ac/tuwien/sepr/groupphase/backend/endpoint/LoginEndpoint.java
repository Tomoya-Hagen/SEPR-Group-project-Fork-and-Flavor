package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/authentication")
public class LoginEndpoint {

    private final UserService userService;

    public LoginEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PermitAll
    @PostMapping
    public String login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        String jwt;
        try {
            jwt = userService.register(userRegisterDto);
        } catch (ValidationException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
        return ResponseEntity.created(URI.create("")).body(jwt);
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Check if a User is logged in", security = @SecurityRequirement(name = "apiKey"))
    @GetMapping("/islogged")
    public boolean isLoggedIn() {
        return true;
    }
}
