package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This is the interface for the persistence layer of Allergens.
 *
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface AllergenRepository extends JpaRepository<Allergen, Long> {
    Optional<Allergen> findByType(String type);

}
