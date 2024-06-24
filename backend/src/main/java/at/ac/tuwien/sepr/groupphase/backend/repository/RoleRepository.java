package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the interface for the persistence layer of Roles.
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Finds a Role entity by its name.
     *
     * @param name The name of the Role entity to be retrieved.
     * @return The Role entity with the given name if it exists, otherwise null.
     */
    Role findByName(String name);

    /**
     * Checks if a Role entity with the given name exists.
     *
     * @param name The name of the Role entity to be checked.
     * @return True if a Role entity with the given name exists, otherwise false.
     */
    Boolean existsByName(String name);
}
