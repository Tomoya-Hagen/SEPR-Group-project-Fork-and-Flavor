package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>{

    @Query("SELECT COALESCE(MAX(i.id),0) FROM Recipe i")
    Long findMaxId();

    @Query("SELECT i FROM Recipe i WHERE i.name LIKE %:name%")
    List<Recipe> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);
}
