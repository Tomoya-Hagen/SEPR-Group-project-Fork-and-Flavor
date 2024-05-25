package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * class in charge of REST requests for Recipes.
 */
@RestController
@RequestMapping(value = "/api/v1/recipes")
public class RecipeEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public RecipeEndpoint(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PermitAll
    @GetMapping(value = "/details/{id}")
    @Operation(summary = "Get recipe details by id")
    public RecipeDetailDto findBy(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/recipe/details/{}", id);
        try {
            return recipeService.getRecipeDetailDtoById(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no recipe found by the given is", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    /**
     * This function updates the recipe with the values specified by the given parameter.
     *
     * @param recipeUpdatedto DTO holding values to update
     * @return ResponseEntity of Recipe with the HTTP status of "No Content"
     */
    @PutMapping
    public ResponseEntity<DetailedRecipeDto> updateRecipe(@RequestBody RecipeUpdateDto recipeUpdatedto) {
        LOGGER.info("PUT /api/v1/recipe + {}", recipeUpdatedto);
        LOGGER.debug("Body of request: {}", recipeUpdatedto);
        ApplicationUser user = userService.findApplicationUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        RecipeDetailDto recipe = recipeService.getRecipeDetailDtoById(recipeUpdatedto.id());
        if (recipe.ownerId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(recipeService.updateRecipe(recipeUpdatedto));
        } catch (Exception e) {
            LOGGER.warn("Error updating recipe book: {}", recipeUpdatedto, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);

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
