package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import java.util.stream.Stream;

public interface RecipeService {

    /**
     * Search for recipes in the persistent data store matching  provided field.
     * The name is considered a match, if the search string is a substring of the field in recipes.
     *
     * @param name the recipe name to use in filtering.
     * @return the recipes where the given fields match.
     */
    Stream<RecipeListDto> searchRecipes(String name);

}
