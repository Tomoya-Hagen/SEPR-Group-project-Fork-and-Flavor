package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for repository of recipe books
 */
@Repository
public interface RecipeBookRepository extends JpaRepository<RecipeBook, Long> {
}
