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
     * Finds an Ingredient entity by its name.
     *
     * @param name The name of the Ingredient.
     * @return An Optional that may contain the Ingredient if one with the given name exists.
     */
    Optional<Ingredient> findByName(String name);


    /**
     * Finds Ingredient entities by a part of their name with a limit on the number of results.
     *
     * @param name The part of the name to search for.
     * @param pageable The pagination information.
     * @return A list of Ingredient entities that have the given part in their name. The number of results is limited by the pageable parameter.
     */
    @Query("SELECT i FROM Ingredient i WHERE i.name LIKE %:name%")
    List<Ingredient> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);


    /**
     * Finds Ingredient entities by a part of their name, ignoring case.
     *
     * @param name The part of the name to search for.
     * @param pageable The pagination information.
     * @return A list of Ingredient entities that have the given part in their name, ignoring case.
     */
    List<Ingredient> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
