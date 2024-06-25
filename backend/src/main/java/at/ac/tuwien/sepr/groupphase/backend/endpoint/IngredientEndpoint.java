package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

/**
 * This is the IngredientEndpoint class. It is a REST controller that handles HTTP requests related to ingredients.
 * It uses the IngredientService to perform operations related to ingredients.
 */
@RestController
@RequestMapping(value = "/api/v1/ingredients")
public class IngredientEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IngredientService ingredientService;

    @Autowired
    public IngredientEndpoint(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Getting ingredients", security = @SecurityRequirement(name = "apiKey"))
    public Stream<IngredientResultDto> get(@RequestParam("name") String name, @RequestParam("limit") int limit) {
        LOGGER.debug("POST /api/v1/ingredients params: {} {}", name, limit);
        return ingredientService.byname(name, limit);
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @Operation(summary = "Getting ingredients", security = @SecurityRequirement(name = "apiKey"))
    public Stream<IngredientResultDto> getAll() {
        LOGGER.debug("POST /api/v1/ingredients all}");
        return ingredientService.all();
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/units")
    @Operation(summary = "Getting units for ingredients", security = @SecurityRequirement(name = "apiKey"))
    public RecipeIngredient.Unit[] getUnits() {
        LOGGER.debug("POST /api/v1/ingredients all}");
        return RecipeIngredient.Unit.values();
    }
}
