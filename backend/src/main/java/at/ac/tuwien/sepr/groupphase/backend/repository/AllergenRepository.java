package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This is the interface for the persistence layer of Allergens.
 * It also includes custom methods for finding allergens by type.
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface AllergenRepository extends JpaRepository<Allergen, Long> {
    /**
     * Finds an Allergen entity by its type.
     *
     * @param type The type of the Allergen.
     * @return An Optional that may contain the Allergen if one with the given type exists.
     */
    Optional<Allergen> findByType(String type);

}
