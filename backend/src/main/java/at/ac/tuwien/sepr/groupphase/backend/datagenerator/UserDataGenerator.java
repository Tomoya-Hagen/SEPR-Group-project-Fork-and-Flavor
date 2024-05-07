package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.entity.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRoleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Profile("generateData")
@Component
public class UserDataGenerator {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(UserRoleRepository userRoleRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    private void generateMessage() {

        if (!userRepository.findAll().isEmpty()) {
            return;
        }

        Role.RoleBuilder rb = new Role.RoleBuilder();
        Role r = rb.withId(1).withroleId("Admin").build();

        UserRole.UserRoleBuilder urrb = new UserRole.UserRoleBuilder();
        UserRole ur = urrb.withroleId(1).withuserId(1).build();

        ApplicationUser.ApplicationUserBuilder aub = new ApplicationUser.ApplicationUserBuilder();
        ApplicationUser au = aub.withEmail("admin@email.com").withPassword(passwordEncoder.encode("password")).withid(1).withUsername("admin").withhasProfilePicture(false).build();

        roleRepository.save(r);
        userRepository.save(au);
        userRoleRepository.save(ur);
    }
}
