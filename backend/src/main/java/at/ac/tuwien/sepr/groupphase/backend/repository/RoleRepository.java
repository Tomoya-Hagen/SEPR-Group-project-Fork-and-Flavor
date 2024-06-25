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
     * Find first Role entries via name.
     *
     * @return role if name found
     */
    Role findByName(String name);

    /**
     * Find first Role entries via name.
     *
     * @return boolean if role name exists
     */
    Boolean existsByName(String name);
}
