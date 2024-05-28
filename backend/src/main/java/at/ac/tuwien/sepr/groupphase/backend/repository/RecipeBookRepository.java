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
 * Interface for repository of recipe books.
 */
@Repository
@DynamicInsert
@DynamicUpdate
public interface RecipeBookRepository extends JpaRepository<RecipeBook, Long> {
    @Query("select distinct RecipeBook from RecipeBook RecipeBook"
        + " where (?1 is null or UPPER(RecipeBook.name) like UPPER('%'||?1||'%'))")
    List<RecipeBook> search(@Param("name") String name);

    @Query("select r from RecipeBook r where r.id between :#{#from} and :#{#to} order by r.id")
    List<RecipeBook> getAllRecipesWithIdFromTo(@Param("from") int from, @Param("to") int to);

    /**
     * This method is used to get all recipe books a user has write access to.
     *
     * @param userId represents the user id of the user.
     * @return all recipe books the user has write access to.
     */
    @Query("select distinct rb from RecipeBook rb left join rb.editors rbe where rb.ownerId=:userId or rbe.id=:userId")
    List<RecipeBook> getRecipeBooksThatAnUserHasWriteAccessToByUserId(@Param("userId") long userId);
}
