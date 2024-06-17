package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * This is the RecipeBookEndpoint class. It is a REST controller that handles HTTP requests related to recipe books.
 * It uses the RecipeBookService to perform operations related to recipe books.
 */
@RestController
@RequestMapping(value = "/api/v1/recipebook")
public class RecipeBookEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeBookService recipeBookService;

    /**
     * The constructor for the RecipeBookEndpoint class.
     *
     * @param recipeBookService The service that performs operations related to recipe books.
     */
    @Autowired
    public RecipeBookEndpoint(RecipeBookService recipeBookService) {
        this.recipeBookService = recipeBookService;
    }

    /**
     * This method handles GET requests to get the details of a recipe book by its ID.
     *
     * @param id The ID of the recipe book.
     * @return The details of the recipe book.
     */
    @PermitAll
    @GetMapping(value = "{id}/details")
    @Operation(summary = "Get recipe details by id")
    public RecipeBookDetailDto findBy(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/recipebook/{}/details", id);
        try {
            return recipeBookService.getRecipeBookDetailDtoById(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no recipe book found by the given is", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @PermitAll
    @GetMapping("")
    @Operation(summary = "Get a list of recipe books")
    public Page<RecipeBookListDto> getListByPageAndStep(@RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "9") int size) {
        LOGGER.info("GET /api/v1/recipebook?page={}&size={}&name={}", page, size, name);
        Pageable pageable = PageRequest.of(page, size);
        if (name != null && !name.isEmpty()) {
            return recipeBookService.searchRecipeBooksByName(name, pageable);
        } else {
            return recipeBookService.getRecipeBooksPageable(pageable);
        }
    }

    @Secured("ROLE_USER")
    @PatchMapping("{recipeBookId}/spoon/{recipeId}")
    public RecipeBookDetailDto spoon(@PathVariable(name = "recipeBookId") Long recipeBookId,
                                     @PathVariable(name = "recipeId") Long recipeId) {
        LOGGER.info("PATCH /api/v1/recipebook/{}/spoon/{}", recipeBookId, recipeId);
        try {
            return recipeBookService.addRecipeToRecipeBook(recipeBookId, recipeId);
        } catch (ForbiddenException e) {
            HttpStatus status = HttpStatus.FORBIDDEN;
            logClientError(status, "you are not allowed to add a recipe to this recipe book", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (DuplicateObjectException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            logClientError(status, "recipe is already in this recipe book", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no recipe book or recipe found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping("user")
    public List<RecipeBookListDto> getRecipeBooksThatAnUserHasWriteAccessTo() {
        LOGGER.info("GET /api/v1/recipebook/user");
        try {
            return recipeBookService.getRecipeBooksThatAnUserHasAccessTo();
        } catch (ForbiddenException e) {
            HttpStatus status = HttpStatus.FORBIDDEN;
            logClientError(status, "you are not allowed to get the recipe books from this user", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no user found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @PermitAll
    @PostMapping
    public ResponseEntity<RecipeBookDetailDto> createRecipeBook(@RequestBody RecipeBookCreateDto recipeBook) {
        LOGGER.trace("Creating recipe book: {}", recipeBook);
        LOGGER.debug("Body of request: {}", recipeBook);
        try {
            LOGGER.debug("Created recipe book: {}", recipeBook);
            return ResponseEntity.status(HttpStatus.CREATED).body(recipeBookService.createRecipeBook(recipeBook));
        } catch (Exception e) {
            LOGGER.warn("Error creating recipe book: {}", recipeBook, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
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
