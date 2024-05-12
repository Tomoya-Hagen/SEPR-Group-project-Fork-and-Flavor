package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Profile("generateData")
@Component
@Order(1)
public class UserDataGenerator {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(RoleRepository roleRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
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
        roleRepository.save(r);
        ApplicationUser.ApplicationUserBuilder aub = new ApplicationUser.ApplicationUserBuilder();
        ApplicationUser au = aub.withemail("admin@email.com").withpassword(passwordEncoder.encode("password"))
            .withid(1).withusername("admin").withhasProfilePicture(false).withRoles(List.of(r)).build();
        userRepository.save(au);
    }
}
