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
    Optional<Recipe> getRecipeById(@Param("id") long id);

    @Query("select r from Recipe r where r.id in :ids")
    List<Recipe> getRecipeByIds(@Param("ids") List<Long> ids);

    @Query("select r from Recipe r where r.id between :#{#from} and :#{#to} order by r.id")
    List<Recipe> getAllRecipesWithIdFromTo(@Param("from") int from, @Param("to") int to);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY r.id LIMIT :limit")
    List<Recipe> findByNamesContainingIgnoreCase(@Param("name") String name, @Param("limit") int limit);
}
