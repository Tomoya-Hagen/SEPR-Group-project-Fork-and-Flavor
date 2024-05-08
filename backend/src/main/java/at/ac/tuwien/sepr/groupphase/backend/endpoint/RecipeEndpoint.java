package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

/**
 * class in charge of REST requests for Recipes
 */
@RestController
@RequestMapping(path = RecipeEndpoint.BASE_PATH)
public class RecipeEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/recipes";
    RecipeService recipeService;

    @Autowired
    public RecipeEndpoint(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * This function updates the recipe with the values specified by the given parameter.
     *
     * @param recipeDetailDto DTO holding values to update
     * @return ResponseEntity of Recipe with the HTTP status of "No Content"
     */
    @PutMapping
    public ResponseEntity<Recipe> updateRecipe(@RequestBody RecipeDetailDto recipeDetailDto) {
        LOGGER.info("PUT " + BASE_PATH + "/{}", recipeDetailDto);
        LOGGER.debug("Body of request: {}", recipeDetailDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(recipeService.updateRecipe(recipeDetailDto));
    }
}
