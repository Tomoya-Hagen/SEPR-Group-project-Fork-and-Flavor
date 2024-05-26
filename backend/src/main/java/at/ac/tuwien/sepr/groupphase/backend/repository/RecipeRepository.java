package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@DynamicInsert
@DynamicUpdate
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> getRecipeById(@Param("id") long id);

    @Query("select r from Recipe r where r.id between :#{#from} and :#{#to} order by r.id")
    List<Recipe> getAllRecipesWithIdFromTo(@Param("from") int from, @Param("to") int to);

    @Query("SELECT i FROM Recipe i WHERE i.name LIKE %:name%")
    List<Recipe> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);

}
