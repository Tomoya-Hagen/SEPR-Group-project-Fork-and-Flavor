package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * gets a list recipe entities by the given range from to.
     *
     * @param from represents the start value of ids which will be returned.
     * @param to represents the end value of ids which will be returned.
     * @return a list of recipes which hava an id in the range @from to @to.
     */
    @Query("select r from Recipe r where r.id between :#{#from} and :#{#to} order by r.id")
    List<Recipe> getAllRecipesWithIdFromTo(@Param("from") int from, @Param("to") int to);
}
