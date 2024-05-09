package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

@DynamicInsert
@DynamicUpdate
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
