package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This is the interface for the persistence layer of Recipes.
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

    Optional<Recipe> findRecipeByName(String name);

    /**
     * gets a list recipe entities by the given range from to.
     *
     * @param from represents the start value of ids which will be returned.
     * @param to   represents the end value of ids which will be returned.
     * @return a list of recipes which hava an id in the range @from to @to.
     */
    List<Recipe> findByIdBetweenOrderById(Long from, Long to);

    /**
     * Finds the maximum id value among all Recipe entities.
     *
     * @return The maximum id value if it exists, otherwise 0.
     */
    @Query("SELECT COALESCE(MAX(i.id),0) FROM Recipe i")
    Long findMaxId();

    /**
     * Retrieves a page of Recipe entities whose names contain the given string, ignoring case. The results are ordered by name.
     *
     * @param name The string to search for in the names of the Recipe entities.
     * @param pageable The pagination information.
     * @return A page of Recipe entities whose names contain the given string, ordered by name.
     */
    Page<Recipe> findByNameContainingIgnoreCaseOrderByName(String name, Pageable pageable);

    /**
     * Retrieves a page of distinct Recipe entities whose names contain the given string, ignoring case, and belong to the given category. The results are ordered by name.
     *
     * @param name The string to search for in the names of the Recipe entities.
     * @param ids The id of the category to which the Recipe entities should belong.
     * @param pageable The pagination information.
     * @return A page of distinct Recipe entities that meet the given criteria, ordered by name.
     */
    @Query("SELECT distinct i FROM Recipe i join FETCH i.categories rc WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%')) AND rc.id = :id")
    Page<Recipe> findByCategoryIdContainingIgnoreCaseOrderByName(@Param("name") String name, @Param("id") long ids, Pageable pageable);

    /**
     * Search for recipes in the persistent data store matching  provided field.
     * The name is considered a match, if the search string is a substring of the field in recipes.
     *
     * @param name the recipe name to use in filtering.
     * @return the recipes where the given fields match.
     */
    @Query("select Recipe from Recipe Recipe where (?1 is null or UPPER(Recipe.name) like UPPER('%'||?1||'%'))")
    List<Recipe> search(@Param("name") String name);

    /**
     * Retrieves a list of Recipe entities whose names contain the given string, ignoring case. The results are ordered by id and limited by the given limit.
     *
     * @param name The string to search for in the names of the Recipe entities.
     * @param limit The maximum number of results to return.
     * @return A list of Recipe entities whose names contain the given string, ordered by id and limited by the given limit.
     */
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY r.id LIMIT :limit")
    List<Recipe> findByNamesContainingIgnoreCase(@Param("name") String name, @Param("limit") int limit);

    /**
     * Retrieves a list of Recipe entities whose names contain the given string. The results are limited by the given pageable object.
     *
     * @param name The string to search for in the names of the Recipe entities.
     * @param pageable The pagination information.
     * @return A list of Recipe entities whose names contain the given string, limited by the given pageable object.
     */
    @Query("SELECT i FROM Recipe i WHERE i.name LIKE %:name%")
    List<Recipe> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);

    /**
     * Retrieves a list of distinct Recipe entities whose names contain the given string and belong to the given category. The results are limited by the given pageable object.
     *
     * @param name The string to search for in the names of the Recipe entities.
     * @param ids The id of the category to which the Recipe entities should belong.
     * @param pageable The pagination information.
     * @return A list of distinct Recipe entities that meet the given criteria, limited by the given pageable object.
     */
    @Query("SELECT distinct i FROM Recipe i join FETCH i.categories rc WHERE i.name LIKE %:name% AND rc.id = :id")
    List<Recipe> findByNameContainingWithLimit(@Param("name") String name, @Param("id") long ids, Pageable pageable);

    /**
     * Retrieves a list of Recipe entities that belong to the given category. The results are limited by the given pageable object.
     *
     * @param ids The id of the category to which the Recipe entities should belong.
     * @param pageable The pagination information.
     * @return A list of Recipe entities that belong to the given category, limited by the given pageable object.
     */
    @Query("SELECT r FROM Recipe r WHERE r.category.id = :categoryId ")
    List<Recipe> findRecipeByCategoryId(@Param("categoryId") long ids, Pageable pageable);

    /**
     * Gets a recipe by id.
     *
     * @param id represents the id of a recipe.
     * @return a recipe entity.
     */
    Recipe findById(long id);

    /**
     * Checks if a recipe with the given id exists.
     *
     * @param id represents the id of the recipe.
     * @return true if a recipe with the given name exists, false otherwise.
     */
    Boolean existsById(long id);

    /**
     * returns all names as list.
     *
     * @return a list of recipe names.
     */
    @Query("SELECT r.name FROM Recipe r")
    List<String> getAllNames();

    /**
     * Returns the first recipe by id.
     *
     * @param id represents the id of a recipe.
     * @return a recipe entity.
     */
    Recipe findFirstById(long id);

    /**
     * Retrieves a list of Recipe entities that are owned by the user with the given id.
     *
     * @param ownerId The id of the owner for which the recipes are to be retrieved.
     * @return A list of Recipe entities that are owned by the user with the given id.
     */
    @Query("SELECT r FROM Recipe r WHERE r.owner.id = :ownerId ")
    List<Recipe> findRecipesByOwnerId(@Param("ownerId") long ownerId);

    /**
     * Retrieves a list of Recipe entities that were forked from the recipe with the given id.
     *
     * @param id The id of the recipe from which the recipes were forked.
     * @return A list of Recipe entities that were forked from the recipe with the given id.
     */
    @Query("SELECT r FROM Recipe r WHERE r.forkedFrom.id = :id")
    List<Recipe> findAllForkedRecipesById(@Param("id") long id);


    /**
     * requests a verify based on the user and the recipe id.
     *
     * @param recipeId id of the recipe.
     * @param userId   id of the user.
     * @return return a Optional of a recipe.
     */
    @Query("SELECT distinct i FROM Recipe i join FETCH i.verifiers rc WHERE i.id = :recipeId AND rc.id = :userId")
    Optional<Recipe> getVerifysByRecipeIdAndUserId(@Param("recipeId") long recipeId, @Param("userId") long userId);

    /**
     * Retrieves a list of Recipe entities that are either owned by the given user or have been rated by the given user with a taste rating of 3 or more.
     *
     * @param owner The ApplicationUser for which the recipes are to be retrieved.
     * @return A list of Recipe entities that meet the given criteria.
     */
    @Query("SELECT r FROM Recipe r Join fetch r.ratings rat Where r.owner = :owner OR (rat.user = :owner AND rat.taste >= 3)")
    List<Recipe> findAllRecipesByGoodInteraction(@Param("owner") ApplicationUser owner);

    /**
     * Retrieves a list of Recipe entities that are either owned by the user with the given id or have been rated by the user with the given id. The results are ordered randomly and limited by the given pageable object.
     *
     * @param owner The id of the user for which the recipes are to be retrieved.
     * @param pageable The pagination information.
     * @return A list of Recipe entities that meet the given criteria, ordered randomly and limited by the given pageable object.
     */
    @Query("SELECT r FROM Recipe r Join fetch r.ratings rat Where r.owner.id = :owner OR rat.user.id = :owner ORDER BY RAND()")
    List<Recipe> findRandomRecipeByInteraction(@Param("owner") long owner, Pageable pageable);

    /**
     * Retrieves a list of all Recipe entities.
     *
     * @return A list of all Recipe entities.
     */
    @Query("SELECT r FROM Recipe r")
    List<Recipe> findAllRecipes();
}
