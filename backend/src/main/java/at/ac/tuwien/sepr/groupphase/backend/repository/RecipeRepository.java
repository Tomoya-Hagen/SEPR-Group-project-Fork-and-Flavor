package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@DynamicInsert
@DynamicUpdate
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    /**
     * Search for recipes in the persistent data store matching  provided field.
     * The name is considered a match, if the search string is a substring of the field in recipes.
     *
     * @param name the recipe name to use in filtering.
     * @return the recipes where the given fields match.
     */
    @Query("select distinct Recipe from Recipe Recipe"
        + " where (Recipe.name is null or UPPER(Recipe.name) like UPPER('%'||Recipe.name||'%'))")
    Collection<Recipe> search(@Param("name") String name);

}
