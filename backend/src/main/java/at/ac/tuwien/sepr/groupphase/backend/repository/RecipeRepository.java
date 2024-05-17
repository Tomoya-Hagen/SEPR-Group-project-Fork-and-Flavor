package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    /**
     * Search for recipes in the persistent data store matching  provided field.
     * The name is considered a match, if the search string is a substring of the field in recipes.
     *
     * @param name the recipe name to use in filtering.
     * @return the recipes where the given fields match.
     */
    @Query("SELECT DISTINCT Recipe FROM Recipe Recipe"
        + " WHERE (Recipe.name IS NULL OR UPPER(Recipe.name) LIKE UPPER('%'||Recipe.name||'%'))")
    Collection<Recipe> search(@Param("name") String name);

}
