package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import org.springframework.data.domain.Pageable;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This is the interface for the persistence layer of Categories.It is a Spring Data JPA repository for Category entities.
 * It extends JpaRepository, which provides methods for CRUD operations.
 * It also includes custom methods for finding Categories by name and getting a range of categories by Id.
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds a Category entity by its name and type.
     *
     * @param name The name of the Category.
     * @param type The type of the Category.
     * @return An Optional that may contain the Category if one with the given name and type exists.
     */
    Optional<Category> findByNameAndType(String name, String type);

    /**
     * Finds Category entities by a part of their name, ignoring case.
     *
     * @param name The part of the name to search for.
     * @param pageable The pagination information.
     * @return A list of Category entities that have the given part in their name, ignoring case.
     */
    List<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Finds Category entities by a part of their name with a limit on the number of results.
     *
     * @param name The part of the name to search for.
     * @param pageable The pagination information.
     * @return A list of Category entities that have the given part in their name. The number of results is limited by the pageable parameter.
     */
    @Query("SELECT i FROM Category i WHERE i.name LIKE %:name%")
    List<Category> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);

    /**
     * Finds Category entities by their name.
     *
     * @param name The name of the Category.
     * @return A list of Category entities with the given name.
     */
    List<Category> findByName(String name);

    /**
     * Finds the first Category entity by its name.
     *
     * @param name The name of the Category.
     * @return The first Category entity with the given name.
     */
    Category findFirstByName(String name);

    /**
     * Finds a Category entity by its id.
     *
     * @param ids The id of the Category.
     * @return An Optional that may contain the Category if one with the given id exists.
     */
    Optional<Category> findById(long ids);
}
