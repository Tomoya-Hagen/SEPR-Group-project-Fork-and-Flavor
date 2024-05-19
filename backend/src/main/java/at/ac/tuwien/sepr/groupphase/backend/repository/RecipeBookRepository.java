package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for repository of recipe books
 */
@Repository
@DynamicInsert
@DynamicUpdate
public interface RecipeBookRepository extends JpaRepository<RecipeBook, Long> {
}
