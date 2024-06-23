package at.ac.tuwien.sepr.groupphase.backend.unittests;

import static org.junit.jupiter.api.Assertions.*;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.Roles;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findUsersByNameFindsOneUserWithTheLetterU() {
        Roles[] roles = Roles.values();
        List<Role> rolesList = new ArrayList<>();
        for (Roles r : roles) {
            Role role = new Role.RoleBuilder().withroleId(r.name()).build();
            rolesList.add(roleRepository.save(role));
        }
        ApplicationUser user1 = new ApplicationUser.ApplicationUserBuilder()
            .withEmail("doesntmatter@hotmail.com")
            .withPassword("password")
            .withUsername("uber")
            .withhasProfilePicture(false)
            .withRoles(rolesList)
            .build();
        ApplicationUser user2 = new ApplicationUser.ApplicationUserBuilder()
            .withEmail("allthesame@cmail.com")
            .withPassword("passkey")
            .withUsername("tber")
            .withhasProfilePicture(false)
            .withRoles(rolesList)
            .build();
        ApplicationUser user3 = new ApplicationUser.ApplicationUserBuilder()
            .withEmail("allahthesame@cmail.com")
            .withPassword("passkey")
            .withUsername("tbero")
            .withhasProfilePicture(false)
            .withRoles(rolesList)
            .build();
        ApplicationUser user4 = new ApplicationUser.ApplicationUserBuilder()
            .withEmail("aluthesame@cmail.com")
            .withPassword("passkey")
            .withUsername("klimakleber-u")
            .withhasProfilePicture(false)
            .withRoles(rolesList)
            .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        List<UserListDto> results = userService.findUsersByName("klima", 20);

        assertEquals(1, results.size());
        System.out.println(results);

        assertEquals("klimakleber-u", results.get(0).name());
    }
}
