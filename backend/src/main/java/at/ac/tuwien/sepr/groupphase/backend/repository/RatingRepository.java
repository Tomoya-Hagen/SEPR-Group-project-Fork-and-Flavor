package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Rating;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This is the interface for the persistence layer of Rating's.It is a Spring Data JPA repository for Rating entities.
 * It extends JpaRepository, which provides methods for CRUD operations.
 * It also includes custom methods for getting for rating by params and getting a range of rating by ID.
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    /**
     * Represents a request that returns a rating that contains all the given values if it exists.
     *
     * @param userId     the userId of the rating.
     * @param recipeId   the recipeId of the rating.
     * @param taste      the taste rating value.
     * @param cost       the cost rating value.
     * @param easeOfPrep the easeOfPrep rating value.
     * @param review     the review of the rating.
     * @return a Optional of a rating.
     */
    @Query("select r from Rating r where r.user.id = :userId and r.recipe.id = :recipeId and r.taste = :taste and r.cost = :cost and r.easeOfPrep = :easeOfPrep and r.review = :review")
    Optional<Rating> findByAllAttributes(
        @Param("userId") long userId,
        @Param("recipeId") long recipeId,
        @Param("taste") long taste,
        @Param("cost") long cost,
        @Param("easeOfPrep") long easeOfPrep,
        @Param("review") String review);

    /**
     * Requests all rating based on the recipeId.
     *
     * @param recipeId id of the recipe.
     * @return a list of all ratings that have the given recipeId.
     */
    @Query("select r from Rating r where r.recipe.id = :recipeId")
    Collection<Rating> getRatingsByRecipeId(@Param("recipeId") long recipeId);

    /**
     * requests a rating based on the user and the recipe id.
     *
     * @param recipeId id of the recipe.
     * @param userId   id of the user.
     * @return return an Optional of a recipe.
     */
    @Query("select r from Rating r where r.recipe.id = :recipeId and r.user.id = :userId")
    Optional<Rating> getRatingsByRecipeIdAndUserId(@Param("recipeId") long recipeId, @Param("userId") long userId);

    /**
     * Retrieves an Optional containing a Rating object associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return Optional containing a Rating object if it exists, empty Optional otherwise.
     */
    @Query("select r from Rating r where r.user.id = :userId ORDER BY r.id LIMIT 10")
    Collection<Rating> getRatingsByUserId(@Param("userId") long userId);

    /**
     * Retrieves a list of ApplicationUser entities who have rated a specific recipe.
     *
     * @param recipe The Recipe entity for which the owners are to be retrieved.
     * @return A list of ApplicationUser entities who have rated the given recipe.
     */
    @Query("select r.user from Rating r where r.recipe = :recipe")
    List<ApplicationUser> getOwnersbyRecipe(@Param("recipe") Recipe recipe);
}
