package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select r from Rating r where r.user.id = :userId and r.recipe.id = :recipeId and r.taste = :taste and r.cost = :cost and r.easeOfPrep = :easeOfPrep and r.review = :review")
    Optional<Rating> findByAllAttributes(
        @Param("userId") long userId,
        @Param("recipeId") long recipeId,
        @Param("taste") long taste,
        @Param("cost") long cost,
        @Param("easeOfPrep") long easeOfPrep,
        @Param("review") String review);

    @Query("select r from Rating r where r.recipe.id = :recipeId")
    Collection<Rating> getRatingsByRecipeId(@Param("recipeId") long recipeId);

    @Query("select r from Rating r where r.recipe.id = :recipeId and r.user.id = :userId")
    Optional<Rating> getRatingsByRecipeIdAndUserId(@Param("recipeId")long recipeId, @Param("userId")long userId);
}
