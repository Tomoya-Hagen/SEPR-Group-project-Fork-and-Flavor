package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserEndPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    UserService userService;

    @Autowired
    public UserEndPoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserListDto>> getUsers(@RequestParam(name = "name") String name, @RequestParam(name = "limit") int limit) {
        LOGGER.info("Getting {} using {}", limit, name);
        LOGGER.debug("Retrieving {} users using {}", limit, name);
        return ResponseEntity.ok(userService.findUsersByName(name, limit));
    }

    @GetMapping("{id}/details")
    public UserDto getUser(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/recipebook/{}/details", id);
        try {
            return userService.findUserById(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no user with id " + id + " found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    /**
     * This method logs client errors.
     *
     * @param status The HTTP status of the error.
     * @param message The error message.
     * @param e The exception that caused the error.
     */
    private void logClientError(HttpStatus status, String message, Exception e) {
        LOGGER.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
    }


}
