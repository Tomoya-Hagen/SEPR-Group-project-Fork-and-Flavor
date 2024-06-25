package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This is the interface for the persistence layer of Nutrition's. It is a Spring Data JPA repository for Nutrition entities.
 * It extends JpaRepository, which provides methods for CRUD operations.
 * It also includes custom methods for finding nutrition by name.
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    /**
     * Finds a Nutrition entity by its name.
     *
     * @param name The name of the Nutrition.
     * @return An Optional that may contain the Nutrition if one with the given name exists.
     */
    Optional<Nutrition> findByName(String name);

}
