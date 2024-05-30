package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This is the interface for the persistence layer of Nutrition's.
 *
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    Optional<Nutrition> findByName(String name);

}
