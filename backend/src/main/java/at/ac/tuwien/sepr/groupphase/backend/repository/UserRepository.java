package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is the interface for the persistence layer of Users.
 *
 */
@Repository
public interface UserRepository  extends JpaRepository<ApplicationUser, Long> {

    /**
     * Find first User entry via email.
     *
     * @return ordered list of all message entries
     */
    @Query("SELECT DISTINCT ApplicationUser FROM ApplicationUser ApplicationUser "
        + "JOIN FETCH ApplicationUser.roles "
        + "WHERE ApplicationUser.email = :email")
    ApplicationUser findFirstUserByEmail(@Param("email") String email);

    /**
     * Find first User entrie via username.
     *
     * @return boolean if user exists
     */
    Boolean existsByUsername(String username);

    /**
     * Find first User entrie via email.
     *
     * @return boolean if user exists
     */
    Boolean existsByEmail(String email);

    /**
     * Find first User entrie via email.
     *
     * @return ordered list of al message entries
     */
    Long findFirstByEmail(String email);

    /**
     * Retrieves a list of ApplicationUser entities whose usernames contain the given string, ignoring case. The results are ordered by id and limited by the given limit.
     *
     * @param name The string to search for in the usernames of the ApplicationUser entities.
     * @param limit The maximum number of results to return.
     * @return A list of ApplicationUser entities whose usernames contain the given string, ordered by id and limited by the given limit.
     */
    @Query("SELECT u FROM ApplicationUser u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) ORDER BY u.id LIMIT :limit")
    List<ApplicationUser> findByNamesContainingIgnoreCase(@Param("username") String name, @Param("limit") int limit);

    /**
     * Retrieves an ApplicationUser entity by its id.
     *
     * @param id The id of the ApplicationUser entity to be retrieved.
     * @return The ApplicationUser entity with the given id if it exists, otherwise null.
     */
    ApplicationUser findFirstById(long id);

    /**
     * Retrieves an ApplicationUser entity by its id with eager loading.
     *
     * @param id The id of the ApplicationUser entity to be retrieved.
     * @return The ApplicationUser entity with the given id if it exists, otherwise null.
     */
    @Query("SELECT u FROM ApplicationUser u WHERE u.id = :id")
    ApplicationUser findFirstByIdWithEagerLoading(@Param("id") Long id);

    /**
     * Updates the password of the ApplicationUser entity with the given id.
     *
     * @param id The id of the ApplicationUser entity to be updated.
     * @param password The new password for the ApplicationUser entity.
     */
    @Transactional
    @Modifying
    @Query("UPDATE ApplicationUser u SET u.password = :password where u.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String password);
}
