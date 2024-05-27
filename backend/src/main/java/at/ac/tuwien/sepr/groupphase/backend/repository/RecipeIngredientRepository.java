package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@DynamicInsert
@DynamicUpdate
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    /**
     * This method searches for a RecipeIngredients by the given recipe id.
     *
     * @param id represents the id of a recipe.
     * @return a list of RecipeIngredient that are used in a recipe.
     */
    List<RecipeIngredient> getRecipeIngredientsByRecipeId(long id);
}
