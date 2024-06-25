package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.WeeklyPlanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

/**
 * This is the interface for the persistence layer of WeeklyPlanner.
 *
 */
@Repository
public interface WeeklyPlannerRepository extends JpaRepository<WeeklyPlanner, Long> {


    WeeklyPlanner findWeeklyPlannerById(Long id);

    /**
     * Find weekPlanner via id, from, to.
     *
     * @return array of weekPlanner
     */
    @Query("SELECT i FROM WeeklyPlanner i WHERE i.recipeBook.id = :id AND i.date <= :to AND i.date >= :from order by i.date ASC")
    WeeklyPlanner[] findWeeklyPlannerByDate(@Param("id") long id, @Param("from") Date from, @Param("to")Date to);
}
