package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * This is the interface for the persistence layer of Ingredients.
 *
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);


    @Query("SELECT i FROM Ingredient i WHERE i.name LIKE %:name%")
    List<Ingredient> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);


    List<Ingredient> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
