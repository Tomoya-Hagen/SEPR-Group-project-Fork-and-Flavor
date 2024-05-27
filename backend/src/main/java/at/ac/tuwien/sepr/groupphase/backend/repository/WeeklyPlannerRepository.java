package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.WeeklyPlanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the interface for the persistence layer of WeeklyPlanner.
 *
 */
@Repository
public interface WeeklyPlannerRepository extends JpaRepository<WeeklyPlanner, Long> {
}
