package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.FullRatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/ratings")
public class RatingEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RatingService ratingService;

    @Autowired
    public RatingEndpoint(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PermitAll
    @GetMapping("/recipe/{id}")
    @Operation(summary = "Get a list of ratings for a recipe")
    public List<RatingListDto> getRatingFromRecipeById(@PathVariable("id") long id) {
        LOGGER.info("GET /api/v1/recipe/{}/rating", id);
        try {
            return ratingService.getRatingsByRecipeId(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no recipe found by the given id", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @PermitAll
    @GetMapping("/user/{id}")
    public List<FullRatingListDto> getRatingFromUserById(@PathVariable("id") long id) {
        LOGGER.info("GET /api/v1/ratings/user/{}", id);
        try {
            return ratingService.getRatingsByUserId(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no user found by the given id", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new Rating", security = @SecurityRequirement(name = "apiKey"))
    public RatingListDto createNewRating(@RequestBody RatingCreateDto ratingCreateDto) {
        LOGGER.info("POST /api/v1/recipe/rating body: {}", ratingCreateDto);
        try {
            return ratingService.createRating(ratingCreateDto);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ValidationException | DuplicateObjectException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    private void logClientError(HttpStatus status, String message, Exception e) {
        LOGGER.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
    }
}
