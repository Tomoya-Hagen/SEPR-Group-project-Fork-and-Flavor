package at.ac.tuwien.sepr.groupphase.backend.repository;


import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the interface for the persistence layer of RecipesStep's.
 *
 */

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
}
