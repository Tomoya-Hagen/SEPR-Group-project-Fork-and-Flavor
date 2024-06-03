package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is the RecipeBookRepository interface. It is a Spring Data JPA repository for RecipeBook entities.
 * It extends JpaRepository, which provides methods for CRUD operations.
 * It also includes custom methods for searching for recipe books by name and getting a range of recipe books by ID.
 */
@Repository
@DynamicInsert
@DynamicUpdate
public interface RecipeBookRepository extends JpaRepository<RecipeBook, Long> {

    /**
     * This method is responsible for searching for recipe books by name.
     * It uses a custom query to select distinct recipe books where the name matches the provided name.
     * The name matching is case-insensitive and allows for partial matches.
     *
     * @param name The name of the recipe book.
     * @return A list of RecipeBook entities that match the search criteria.
     */
    @Query("select distinct RecipeBook from RecipeBook RecipeBook"
        + " where (?1 is null or UPPER(RecipeBook.name) like UPPER('%'||?1||'%'))")
    List<RecipeBook> search(@Param("name") String name);

    /**
     * This method is responsible for getting a range of recipe books by ID.
     * It uses a custom query to select recipe books where the ID is between the provided from and to values.
     * The results are ordered by ID.
     *
     * @param from The start of the ID range.
     * @param to The end of the ID range.
     * @return A list of RecipeBook entities that are within the specified ID range.
     */
    List<RecipeBook> findByIdBetweenOrderById(Long from, Long to);

    /**
     * This method is used to get all recipe books a user has write access to.
     *
     * @param userId represents the user id of the user.
     * @return all recipe books the user has write access to.
     */
    @Query("SELECT rb FROM RecipeBook rb WHERE rb.owner.id = :userId OR :userId IN (SELECT u.id FROM rb.editors u)")
    List<RecipeBook> findRecipeBooksByOwnerOrSharedUser(@Param("userId") long userId);

    /**
     * This method is used to check if a recipe book with the given name exists.
     *
     * @param name represents the name of the recipe book.
     * @return true if a recipe book with the given name exists, false otherwise.
     */
    Boolean existsByName(String name);

}
