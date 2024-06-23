package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.BadgeService;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.Roles;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BadgeServiceImpl implements BadgeService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public BadgeServiceImpl(UserRepository userRepository,
                            RoleRepository roleRepository,
                            EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    @Override
    public void addRoleToUser(ApplicationUser user, Roles role) {
        if (!hasUserTheRole(user, role)) {
            setRoleForUser(user, role);
            emailService.sendSimpleEmail(user.getEmail(), "Neuer Badge", "Gratulation, du bist jetzt " + role.name() + "!");
        }
    }

    private void setRoleForUser(ApplicationUser user, Roles role) {
        Role newRole = roleRepository.findByName(role.name());
        List<Role> roles = new ArrayList<>(user.getRoles());
        roles.add(newRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    private boolean hasUserTheRole(ApplicationUser user, Roles role) {
        List<Role> userRoles = user.getRoles();
        return userRoles.stream().anyMatch(r -> r.getName().equals(role.name()));
    }
}
