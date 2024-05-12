package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
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

@RestController
@RequestMapping(value = "/api/v1/recipes")
public class RecipeEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeService recipeService;

    @Autowired
    public RecipeEndpoint(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PermitAll
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get recipe details by id")
    public RecipeDetailDto findBy(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/recipe/{}", id);
        try {
            return recipeService.getRecipeDetailDtoById(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no recipe found by the given is", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @PermitAll
    @GetMapping("/")
    @Operation(summary = "Get a list of recipes")
    public List<RecipeListDto> getListByPageAndStep(@RequestParam(name = "page") int page, @RequestParam(name = "step") int step) {
        LOGGER.info("GET /api/v1/recipe?page={}&step={}", page, step);
        return recipeService.getRecipesFromPageInSteps(page, step);
    }

    private void logClientError(HttpStatus status, String message, Exception e) {
        LOGGER.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
    }
}
