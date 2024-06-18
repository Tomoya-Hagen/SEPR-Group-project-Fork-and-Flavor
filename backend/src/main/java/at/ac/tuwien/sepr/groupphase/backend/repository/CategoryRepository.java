package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import org.springframework.data.domain.Pageable;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This is the interface for the persistence layer of Categories.
 *
 */
@DynamicInsert
@DynamicUpdate
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndType(String name, String type);

    List<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT i FROM Category i WHERE i.name LIKE %:name%")
    List<Category> findByNameContainingWithLimit(@Param("name") String name, Pageable pageable);

    List<Category> findByName(String name);

    Optional<Category> findById(long ids);
}
