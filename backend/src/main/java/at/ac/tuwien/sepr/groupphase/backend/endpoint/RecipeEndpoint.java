package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Stream;

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
    @PutMapping("/{id}")
    public ResponseEntity<DetailedRecipeDto> updateRecipe(@PathVariable("id") Long id, @RequestBody RecipeUpdateDto recipeUpdatedto) {
        LOGGER.info("PUT /api/v1/recipe/ + {} + {}", id, recipeUpdatedto);
        LOGGER.debug("Body of request: {}", recipeUpdatedto);
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(recipeService.updateRecipe(recipeUpdatedto));
        } catch (Exception e) {
            LOGGER.warn("Error updating recipe book: {}", recipeUpdatedto, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PermitAll
    @GetMapping("/")
    @Operation(summary = "Get a list of recipes")
    public List<RecipeListDto> getListByPageAndStep(@RequestParam(name = "page") int page, @RequestParam(name = "step") int step) {
        LOGGER.info("GET /api/v1/recipe?page={}&step={}", page, step);
        return recipeService.getRecipesFromPageInSteps(page, step);
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/simple")
    @Operation(summary = "Getting simple recipes", security = @SecurityRequirement(name = "apiKey"))
    public Stream<SimpleRecipeResultDto> get(@RequestParam("name") String name, @RequestParam("limit") int limit) {
        LOGGER.info("POST /api/v1/recipe params: {} {}", name, limit);
        return recipeService.byname(name, limit);
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new Recipe", security = @SecurityRequirement(name = "apiKey"))
    public DetailedRecipeDto createnew(@Valid @RequestBody RecipeCreateDto recipeDto) {
        LOGGER.info("POST /api/v1/recipe body: {}", recipeDto);
        try {
            return recipeService.createRecipe(recipeDto);
        } catch (Exception e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @GetMapping
    public ResponseEntity<List<RecipeListDto>> getRecipesByNames(@RequestParam(name = "name") String name, @RequestParam(name = "limit") int limit) {
        LOGGER.info("Getting {} using {}", limit, name);
        LOGGER.debug("Retrieving {} recipes using {}", limit, name);
        return ResponseEntity.ok(recipeService.getRecipesByNames(name, limit));
    }

    private void logClientError(HttpStatus status, String message, Exception e) {
        LOGGER.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
    }

}
