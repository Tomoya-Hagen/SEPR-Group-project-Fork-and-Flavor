package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recipes")
public class RecipeEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeService recipeService;

    @Autowired
    public RecipeEndpoint(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * This method handles GET requests to search for recipe books by name.
     *
     * @param name The name of the recipe book.
     * @return A list of recipe books that match the search criteria.
     */
    @PermitAll
    @GetMapping("/search")
    @Operation(summary = "Get a list of the searched recipe")
    public List<RecipeListDto> search(@RequestParam(name = "name") String name) {
        LOGGER.info("GET /api/v1/recipe/search");
        return recipeService.searchRecipe(name);
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
