package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    /**
     * This method handles GET requests to get a list of all recipe books.
     *
     * @return A list of all recipe books.
     */
    @PermitAll
    @GetMapping("")
    @Operation(summary = "Get a list of all recipe books")
    public List<RecipeBookListDto> getRecipeBookList() {
        LOGGER.info("GET /api/v1/recipebook");
        return recipeBookService.getRecipeBooks();
    }

    /**
     * This method handles GET requests to get a list of recipe books by page and step.
     *
     * @param page The page number.
     * @param step The step size.
     * @return A list of recipe books.
     */
    @PermitAll
    @GetMapping("/")
    @Operation(summary = "Get a list of recipe books")
    public List<RecipeBookListDto> getListByPageAndStep(@RequestParam(name = "page") int page, @RequestParam(name = "step") int step) {
        LOGGER.info("GET /api/v1/recipebook?page={}&step={}", page, step);
        return recipeBookService.getRecipeBooksFromPageInSteps(page, step);
    }

    /**
     * This method handles GET requests to search for recipe books by name.
     *
     * @param name The name of the recipe book.
     * @return A list of recipe books that match the search criteria.
     */
    @PermitAll
    @GetMapping("/search")
    @Operation(summary = "Get a list of the searched recipe books")
    public List<RecipeBookListDto> search(@RequestParam(name = "name") String name) {
        LOGGER.info("GET /api/v1/recipebook/search");
        return recipeBookService.searchRecipeBooks(name);
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
