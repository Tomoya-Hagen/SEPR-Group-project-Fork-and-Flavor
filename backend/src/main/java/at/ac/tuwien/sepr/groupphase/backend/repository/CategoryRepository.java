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
     * gets a category by its name and type.
     *
     * @param name represents the name of a category.
     * @param type represents the type of a category.
     * @return an Optional object which contains the category if one exists by the given name and type.
     */
    Optional<Category> findByNameAndType(String name, String type);

    /**
     * gets a list category entities by the given range from name.
     *
     * @param name represents name which will be returned.
     * @param pageable The page information.
     * @return A Pageable object that contains the details of category that match the name.
     */
    List<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);


    @Query("SELECT i FROM Category i WHERE i.name LIKE %:name%")
    List<Category> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);

    /**
     * gets a list category entities by the given range from name.
     *
     * @param name represents name which will be returned.
     * @return A list of category that match the name.
     */
    List<Category> findByName(String name);

    /**
     * gets a category by its name and type.
     *
     * @param name represents the name of a category.
     * @return category exists by the given name.
     */
    Category findFirstByName(String name);

    /**
     * gets a category by its ids.
     *
     * @param ids represents the name of a category.
     * @return an Optional object which contains the category if one exists by the given ids.
     */
    Optional<Category> findById(long ids);
}
