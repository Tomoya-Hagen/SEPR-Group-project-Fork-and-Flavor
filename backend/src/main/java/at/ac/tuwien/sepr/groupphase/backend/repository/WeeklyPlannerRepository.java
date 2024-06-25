package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.WeeklyPlanner;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This is the interface for the persistence layer of WeeklyPlanner.
 *
 */
@Repository
public interface WeeklyPlannerRepository extends JpaRepository<WeeklyPlanner, Long> {

    /**
     * Retrieves a WeeklyPlanner entity by its id.
     *
     * @param id The id of the WeeklyPlanner entity to be retrieved.
     * @return The WeeklyPlanner entity with the given id if it exists, otherwise null.
     */
    WeeklyPlanner findWeeklyPlannerById(Long id);

    /**
     * Retrieves an array of WeeklyPlanner entities that belong to the RecipeBook with the given id and whose dates are within the given range. The results are ordered by date in ascending order.
     *
     * @param id The id of the RecipeBook to which the WeeklyPlanner entities should belong.
     * @param from The start date of the range within which the dates of the WeeklyPlanner entities should fall.
     * @param to The end date of the range within which the dates of the WeeklyPlanner entities should fall.
     * @return An array of WeeklyPlanner entities that meet the given criteria, ordered by date in ascending order.
     */
    @Query("SELECT i FROM WeeklyPlanner i WHERE i.recipeBook.id = :id AND i.date <= :to AND i.date >= :from order by i.date ASC")
    WeeklyPlanner[] findWeeklyPlannerByDate(@Param("id") long id, @Param("from") Date from, @Param("to")Date to);

    @Query("SELECT DISTINCT w.date FROM WeeklyPlanner w WHERE w.recipeBook.id = :id AND w.date > :startDate ORDER BY w.date")
    List<Date> findNextFilledDates(@Param("id") long id, @Param("startDate") Date startDate, Pageable pageable);

    @Query("SELECT w FROM WeeklyPlanner w WHERE w.recipeBook.id = :id AND w.date IN :dates ORDER BY w.date")
    WeeklyPlanner[] findByDates(@Param("id") long id, @Param("dates") List<Date> dates);

    @Query("SELECT w from WeeklyPlanner w WHERE w.recipeBook.id = :id AND (w.date BETWEEN :startDate AND :endDate)")
    List<WeeklyPlanner> getWeeklyPlannerItemThatIsInTheGivenTimeFromTheGivenRecipeBook(
        @Param("id") long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate")LocalDate endDate);
}
