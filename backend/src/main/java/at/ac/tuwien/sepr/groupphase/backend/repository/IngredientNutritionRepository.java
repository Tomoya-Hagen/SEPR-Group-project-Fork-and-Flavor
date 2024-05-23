package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.IngredientNutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the interface for the persistence layer of IngredientNutrition's.
 *
 */
@Repository
public interface IngredientNutritionRepository extends JpaRepository<IngredientNutrition, Long> {
}
