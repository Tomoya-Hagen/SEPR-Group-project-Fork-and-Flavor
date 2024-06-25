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
 * This is the interface for the persistence layer of Ingredients.It is a Spring Data JPA repository for Ingredient entities.
 * It extends JpaRepository, which provides methods for CRUD operations.
 * It also includes custom methods for finding Ingredients by name and getting a range of ingredients by Id.
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * gets a ingredients by its name
     *
     * @param name represents the type of a ingredient.
     * @return an Optional object which contains the ingredient if one exists by the given name.
     */
    Optional<Ingredient> findByName(String name);

    /**
     * gets a list ingredients entities by the given range from name.
     *
     * @param name represents name which will be returned.
     * @param pageable The page information.
     * @return A Pageable object that contains the details of ingredient that match the name.
     */
    @Query("SELECT i FROM Ingredient i WHERE i.name LIKE %:name%")
    List<Ingredient> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);


    List<Ingredient> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
