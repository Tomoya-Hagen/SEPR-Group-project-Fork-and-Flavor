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
     * find list of user via username.
     *
     * @param name username represents
     * @param limit limit user id represents
     * @return the details of list of recipe that match the user.
     */
    @Query("SELECT u FROM ApplicationUser u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) ORDER BY u.id LIMIT :limit")
    List<ApplicationUser> findByNamesContainingIgnoreCase(@Param("username") String name, @Param("limit") int limit);

    /**
     * Find first User entry via id.
     *
     * @return ordered list of al message entries
     */
    ApplicationUser findFirstById(long id);

    @Query("SELECT u FROM ApplicationUser u WHERE u.id = :id")
    ApplicationUser findFirstByIdWithEagerLoading(@Param("id") Long id);

    /**
     * Update user to id and from password.
     *
     * @param id user id represents
     * @param password user password represents
     */
    @Transactional
    @Modifying
    @Query("UPDATE ApplicationUser u SET u.password = :password where u.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String password);
}
