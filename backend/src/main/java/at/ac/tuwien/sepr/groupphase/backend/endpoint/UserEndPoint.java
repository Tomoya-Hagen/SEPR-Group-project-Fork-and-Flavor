package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserPasswordChangeDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.BadgeService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * This is the UserEndpoint class. It is a REST controller that handles HTTP requests related to users.
 * It uses the UserService to perform operations related to users.
 */
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserEndPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final BadgeService badgeService;

    public UserEndPoint(UserService userService,
                        BadgeService badgeService) {
        this.userService = userService;
        this.badgeService = badgeService;
    }

    @Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<List<UserListDto>> getUsers(@RequestParam(name = "name") String name, @RequestParam(name = "limit") int limit) {
        LOGGER.info("Getting {} using {}", limit, name);
        LOGGER.debug("Retrieving {} users using {}", limit, name);
        return ResponseEntity.ok(userService.findUsersByName(name, limit));
    }

    @PermitAll
    @GetMapping("{id}/details")
    public UserDto getUser(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/users/{}/details", id);
        try {
            return userService.findUserById(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no user with id " + id + " found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @PermitAll
    @GetMapping("{id}/recipebooks")
    public List<RecipeBookListDto> getRecipeBooksByUserId(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/users/{}/recipebooks", id);
        return userService.findRecipeBooksByUserId(id);
    }

    @PermitAll
    @GetMapping("{id}/recipes")
    public List<RecipeListDto> getRecipesByUserId(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/users/{}/recipes", id);
        return userService.findRecipesByUserId(id);
    }

    @Secured("ROLE_USER")
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser() {
        try {
            UserDto currentUser = userService.getCurrentUser();
            return new ResponseEntity<>(currentUser, HttpStatus.OK);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no user with found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping("/badge")
    public List<String> getBadgesOfCurrentUser() {
        return badgeService.getBadgesOfCurrentUser();
    }

    @PermitAll
    @GetMapping("{id}/badge")
    public List<String> getBadgesOfUser(@PathVariable(name = "id") Long id) {
        return badgeService.getBadgesOfUser(id);
    }

    @Secured("ROLE_USER")
    @PatchMapping("/changePassword/{id}")
    public void changePassword(@PathVariable(name = "id") Long id, @RequestBody UserPasswordChangeDto userPasswordChangeDto) {
        LOGGER.info("PATCH /api/v1/users/changePassword/{}", id);
        try {
            userService.changePassword(id, userPasswordChangeDto);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no user with specified id found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (BadCredentialsException | ValidationException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            logClientError(status, "password is not valid", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    /**
     * This method logs client errors.
     *
     * @param status  The HTTP status of the error.
     * @param message The error message.
     * @param e       The exception that caused the error.
     */
    private void logClientError(HttpStatus status, String message, Exception e) {
        LOGGER.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
    }
}
