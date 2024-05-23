package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<ApplicationUser, Long> {


    /**
     * Find first User entry via email.
     *
     * @return ordered list of al message entries
     */
    @Query("SELECT DISTINCT ApplicationUser FROM ApplicationUser ApplicationUser "
        + "JOIN FETCH ApplicationUser.roles "
        + "WHERE ApplicationUser.email = :email")
    ApplicationUser findFirstUserByEmail(@Param("email") String email);

}
