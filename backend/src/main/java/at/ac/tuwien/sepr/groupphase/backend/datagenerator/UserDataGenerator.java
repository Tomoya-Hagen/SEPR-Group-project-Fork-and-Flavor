package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class UserDataGenerator {

    // Autowired dependencies
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(RoleRepository roleRepository,
                             UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        generateTestData();
    }

    public void generateTestData() {
        String[] roles = {"Admin", "User", "Contributor", "Cook", "StarCook"};
        String[] usernames = {"admin", "user", "contributor", "cook", "starcook"};
        String[] emails = {"admin@email.com", "user@email.com", "contributor@email.com", "cook@email.com", "starcook@email.com"};

        // Create and save roles
        List<Role> savedRoles = new ArrayList<>();
        for (String s : roles) {
            Role role = new Role.RoleBuilder().withroleId(s).build();
            savedRoles.add(roleRepository.save(role));
        }

        // Create and save users and their roles
        for (int i = 0; i < usernames.length; i++) {
            // Check if a user with the same username already exists
            if (!userRepository.existsByUsername(usernames[i])) {
                List<Role> userRoles = new ArrayList<>();
                userRoles.add(savedRoles.get(i));
                ApplicationUser user = new ApplicationUser.ApplicationUserBuilder()
                    .withEmail(emails[i])
                    .withPassword(passwordEncoder.encode("password"))
                    .withUsername(usernames[i])
                    .withhasProfilePicture(false)
                    .withRoles(userRoles)
                    .build();
                userRepository.save(user);

            }
        }
    }
}
