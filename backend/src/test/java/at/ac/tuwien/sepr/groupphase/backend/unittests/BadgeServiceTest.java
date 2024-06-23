package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.BadgeService;
import at.ac.tuwien.sepr.groupphase.backend.service.Roles;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class BadgeServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private RoleRepository roleRepository;
    @Test
    void addRoleToUserThatAlreadyHasThisRoleShouldNotChangeAnything(){
        ApplicationUser user = userRepository.findFirstById(1L);
        List<Role> roles = user.getRoles();
        Role role = roleRepository.findByName(Roles.StarCook.name());
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        int rolesCount = user.getRoles().size();
        Assertions.assertTrue(user.getRoles().contains(role));
        badgeService.addRoleToUser(user,Roles.StarCook);
        Assertions.assertAll(
            () -> Assertions.assertEquals(rolesCount, user.getRoles().size()),
            () -> Assertions.assertTrue(user.getRoles().contains(role))
        );
    }

    @Test
    @Disabled
    void addRoleToUserShouldAddRoleToUser(){
        ApplicationUser user = userRepository.findFirstById(1L);
        Role role = roleRepository.findByName(Roles.StarCook.name());
        int rolesCount = user.getRoles().size();
        Assertions.assertFalse(user.getRoles().contains(role));
        badgeService.addRoleToUser(user,Roles.StarCook);
        Assertions.assertAll(
            () -> Assertions.assertEquals(rolesCount + 1, user.getRoles().size()),
            () -> Assertions.assertTrue(user.getRoles().contains(role))
        );
    }
}
