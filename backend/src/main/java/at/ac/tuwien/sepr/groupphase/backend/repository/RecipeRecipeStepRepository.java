package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRecipeStepRepository extends JpaRepository<RecipeRecipeStep, Long> {


    /**
     * Finds the maximum id value among all RecipeDescriptionStep entities.
     *
     * @return The maximum id value if it exists, otherwise 0.
     */
    @Query("SELECT COALESCE(MAX(id), 0) FROM RecipeDescriptionStep")
    Long findMaxId();
}