package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This is the interface for the persistence layer of Recipes.
 *
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * gets a recipe by its id.
     *
     * @param id represents the id of a recipe.
     * @return an Optional object which contains the recipe if one exists by the given id.
     */
    Optional<Recipe> getRecipeById(@Param("id") long id);

    @Query("select r from Recipe r where r.id in :ids")
    List<Recipe> getRecipeByIds(@Param("ids") List<Long> ids);

    /**
     * gets a list recipe entities by the given range from to.
     *
     * @param from represents the start value of ids which will be returned.
     * @param to represents the end value of ids which will be returned.
     * @return a list of recipes which hava an id in the range @from to @to.
     */
    @Query("select r from Recipe r where r.id between :#{#from} and :#{#to} order by r.id")
    List<Recipe> getAllRecipesWithIdFromTo(@Param("from") int from, @Param("to") int to);

    @Query("SELECT COALESCE(MAX(i.id),0) FROM Recipe i")
    Long findMaxId();

    List<Recipe> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Search for recipes in the persistent data store matching  provided field.
     * The name is considered a match, if the search string is a substring of the field in recipes.
     *
     * @param name the recipe name to use in filtering.
     * @return the recipes where the given fields match.
     */
    @Query("select Recipe from Recipe Recipe where (?1 is null or UPPER(Recipe.name) like UPPER('%'||?1||'%'))")
    List<Recipe> search(@Param("name") String name);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY r.id LIMIT :limit")
    List<Recipe> findByNamesContainingIgnoreCase(@Param("name") String name, @Param("limit") int limit);

    @Query("SELECT i FROM Recipe i WHERE i.name LIKE %:name%")
    List<Recipe> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);

    Recipe findById(long id);

    Boolean existsByName(String name);

    Recipe findByName(String name);

    Boolean existsById(long id);

    @Query("SELECT r.name FROM Recipe r")
    List<String> getAllNames();

    Recipe findFirstById(long id);
}
