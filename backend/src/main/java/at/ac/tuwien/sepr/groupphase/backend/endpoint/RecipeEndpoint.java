package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Stream;

/**
 * This is the RecipeEndpoint class. It is a REST controller that handles HTTP requests related to recipes.
 * It uses the RecipeService to perform operations related to recipes.
 */
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
    @GetMapping(value = "/details/{id}")
    @Operation(summary = "Get recipe details by id")
    public ResponseEntity<RecipeDetailDto> findBy(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/recipe/details/{}", id);
        try {
            RecipeDetailDto recipeDetailDto = recipeService.getRecipeDetailDtoById(id);
            return ResponseEntity.ok(recipeDetailDto);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no recipe found by the given id", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @PermitAll
    @GetMapping(value = "/edit/{id}")
    @Operation(summary = "Get recipe details by id")
    public RecipeDetailDto editBy(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/recipe/details/{}", id);
        try {
            return recipeService.getRecipeDetailDtoById(id, false);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no recipe found by the given is", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @PermitAll
    @GetMapping(value = "/{id}/goesWellWith")
    @Operation(summary = "Get recipes that go well with the recipe with the given id")
    public Page<RecipeListDto> goesWellWith(@PathVariable(name = "id") Long id,
                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "3") int size) {
        LOGGER.info("GET /api/v1/recipe/goesWellWith/{}", id);
        try {
            return recipeService.getRecipesThatGoWellWith(id, PageRequest.of(page, size));
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "no recipe found by the given is", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @PutMapping("/{id}/goesWellWith")
    @Operation(summary = "Add a recipes that go well with the recipe with the given id")
    public RecipeDetailDto addGoesWellWith(@PathVariable(name = "id") Long id, @RequestBody List<RecipeListDto> goWellWith) {
        LOGGER.info("PUT /api/v1/recipe/goesWellWith/{}", id);
        try {
            return recipeService.addGoesWellWith(id, goWellWith);
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
    @Secured("ROLE_USER")
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

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/simple")
    @Operation(summary = "Getting simple recipes", security = @SecurityRequirement(name = "apiKey"))
    public Stream<SimpleRecipeResultDto> get(@RequestParam("name") String name, @RequestParam("categoryId") long categoryId, @RequestParam("limit") int limit) {
        LOGGER.info("POST /api/v1/recipe params: {} {} {}", name, categoryId, limit);
        RecipeSearchDto searchDto = new RecipeSearchDto(name, categoryId);
        if (categoryId != 0) {
            return recipeService.bynamecategories(searchDto, limit);
        } else {
            return recipeService.byname(name, limit);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new Recipe", security = @SecurityRequirement(name = "apiKey"))
    public DetailedRecipeDto createnew(@Valid @RequestBody RecipeCreateDto recipeDto) {
        LOGGER.info("POST /api/v1/recipe body: {}", recipeDto);
        try {
            return recipeService.createRecipe(recipeDto);
        } catch (ValidationException | RecipeStepNotParsableException | RecipeStepSelfReferenceException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/fork/{id}")
    @Operation(summary = "Fork a new Recipe", security = @SecurityRequirement(name = "apiKey"))
    public DetailedRecipeDto fork(@Valid @RequestBody RecipeCreateDto recipeDto, @PathVariable("id") int id) {
        LOGGER.info("POST /api/v1/recipe body: {}", recipeDto);
        try {
            return recipeService.forkRecipe(recipeDto, id);
        } catch (ValidationException | RecipeStepNotParsableException | RecipeStepSelfReferenceException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @PermitAll
    @GetMapping
    @Operation(summary = "Get a list of recipes")
    public Page<RecipeListDto> getRecipesByName(@RequestParam(name = "name", required = false, defaultValue = "") String name,
                                                @RequestParam(name = "categoryId", required = false, defaultValue = "0") long categoryId,
                                                @RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "9") int size) {
        LOGGER.info("GET /api/v1/recipes?page={}&size={}&name={}&categoryId={}", page, size, name, categoryId);
        Pageable pageable = PageRequest.of(page, size);

        RecipeSearchDto searchDto = new RecipeSearchDto(name, categoryId);
        if (categoryId != 0) {
            return recipeService.getRecipesByNameCategories(searchDto, pageable);
        } else {
            return recipeService.getRecipesByName(name, pageable);
        }
    }

    @PermitAll
    @GetMapping("/best")
    @Operation(summary = "Get a list of recipes")
    public Page<RecipeListDto> getListByBest(@RequestParam("size") int size) {
        LOGGER.info("POST /api/v1/recipe/best params: {} ", size);
        return recipeService.byBest(size);
    }

    @PutMapping("/verify/{id}")
    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "verify a recipe")
    public void verifyRecipe(@PathVariable("id") long id) {
        try {
            recipeService.verifyRecipe(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ForbiddenException e) {
            HttpStatus status = HttpStatus.FORBIDDEN;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ValidationException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @GetMapping("/hasVerified/{id}")
    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Check if a recipe has been verified by a user")
    public boolean hasVerified(@PathVariable("id") long id) {
        try {
            return recipeService.hasVerified(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping("/recommended")
    @Operation(summary = "Get a list of recommended recipes")
    public List<RecipeListDto> getRecipesByRecommendation() {
        LOGGER.info("GET /api/v1/recipes");
        return recipeService.getRecipesByRecommendation();
    }

    private void logClientError(HttpStatus status, String message, Exception e) {
        LOGGER.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
    }
}
