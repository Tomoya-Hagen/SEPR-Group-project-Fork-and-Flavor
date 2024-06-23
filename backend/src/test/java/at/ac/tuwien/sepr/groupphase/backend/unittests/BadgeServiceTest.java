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
    void getAllBadgesfromUserShouldGetRoles() {

        List<Role> rolesList = new ArrayList<>();
        Role r = new Role();
        r.setId(1);
        r.setName("StarCook");

        rolesList.add(r);
        Role r2 = new Role();
        r.setId(1);
        r.setName("Contributor");

        rolesList.add(r2);
        Role r3 = new Role();
        r.setId(1);
        r.setName("Cook");
        rolesList.add(r3);

        List<Roles> rolesL = new ArrayList<>();
        Roles rL = Roles.Cook;
        rolesL.add(rL);
        Roles rL2 = Roles.Contributor;
        rolesL.add(rL2);
        Roles rL3 = Roles.StarCook;
        rolesL.add(rL3);

        ApplicationUser user = new ApplicationUser.ApplicationUserBuilder()
                .withEmail("allahthesame@cmail.com")
                .withPassword("passkey")
                .withUsername("tbero")
                .withhasProfilePicture(false)
                .withRoles(rolesList)
                .build();

        Assertions.assertTrue(userManager.hasUserRole(user,rL));

        userManager.getCurrentUser();
        badgeService.addRoleToUser(user, rL);

        Assertions.assertTrue(user.getRoles().contains(r2));
    }


}
