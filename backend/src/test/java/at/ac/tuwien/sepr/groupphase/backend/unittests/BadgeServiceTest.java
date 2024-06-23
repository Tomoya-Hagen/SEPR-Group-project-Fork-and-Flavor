package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.BadgeService;
import at.ac.tuwien.sepr.groupphase.backend.service.Roles;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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

    @Autowired
    private UserManager userManager;

    @Test
    void addRoleToUserThatAlreadyHasThisRoleShouldNotChangeAnything() {
        ApplicationUser user = userRepository.findFirstById(1L);
        List<Role> roles = user.getRoles();
        Role role = roleRepository.findByName(Roles.StarCook.name());
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        int rolesCount = user.getRoles().size();
        Assertions.assertTrue(user.getRoles().contains(role));
        badgeService.addRoleToUser(user, Roles.StarCook);
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(rolesCount, user.getRoles().size()),
                () -> Assertions.assertTrue(user.getRoles().contains(role))
        );
    }

    @Test
    void addRoleToUserShouldAddRoleToUser() {
        ApplicationUser user = userRepository.findFirstById(1L);
        Role role = roleRepository.findByName(Roles.StarCook.name());
        int rolesCount = user.getRoles().size();
        Assertions.assertTrue(user.getRoles().contains(role));
        badgeService.addRoleToUser(user, Roles.StarCook);
        Assertions.assertAll(
                () -> Assertions.assertEquals(rolesCount + 1, user.getRoles().size()),
                () -> Assertions.assertTrue(user.getRoles().contains(role))
        );
    }

    @Test
    void getAllBadgesFromUserShouldGetRoles() {
        ApplicationUser user = userRepository.findFirstById(1L);
        List<Role> roles = new ArrayList<>();
        user.setRoles(roles);
        userRepository.save(user);
        Assertions.assertAll(
                ()->Assertions.assertFalse(user.getRoles().contains(roleRepository.findByName(Roles.Contributor.name()))),
                ()->Assertions.assertFalse(user.getRoles().contains(roleRepository.findByName(Roles.Cook.name()))),
                ()->Assertions.assertFalse(user.getRoles().contains(roleRepository.findByName(Roles.StarCook.name())))
        );
        for (Roles role : Roles.values()) {
            badgeService.addRoleToUser(user, role);
        }
        Assertions.assertAll(
                ()->Assertions.assertTrue(user.getRoles().contains(roleRepository.findByName(Roles.Contributor.name()))),
                ()->Assertions.assertTrue(user.getRoles().contains(roleRepository.findByName(Roles.Cook.name()))),
                ()->Assertions.assertTrue(user.getRoles().contains(roleRepository.findByName(Roles.StarCook.name())))
        );
    }


}
