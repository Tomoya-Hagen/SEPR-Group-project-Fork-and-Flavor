package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Rating;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.BadgeService;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.Roles;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
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
    private final RecipeRepository recipeRepository;
    private final UserManager userManager;

    public BadgeServiceImpl(UserRepository userRepository,
                            RoleRepository roleRepository,
                            EmailService emailService,
                            RecipeRepository recipeRepository,
                            UserManager userManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.recipeRepository = recipeRepository;
        this.userManager = userManager;
    }

    @Override
    public void addRoleToUser(ApplicationUser user, Roles role) {
        if (role.equals(Roles.StarCook) && !hasUserRequirementsToBeaStarCook(user)) {
            return;
        }
        if (!hasUserTheRole(user, role)) {
            setRoleForUser(user, role);
            emailService.sendSimpleEmail(user.getEmail(), "Neuer Badge", "Gratulation, du bist jetzt " + role.name() + "!");
        }
    }

    private boolean hasUserRequirementsToBeaStarCook(ApplicationUser user) {
        List<Recipe> recipes = recipeRepository.findRecipesByOwnerId(user.getId());
        List<Rating> ratings = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ratings.addAll(recipe.getRatings());
            if (ratings.size() > 20) {
                break;
            }
        }
        return recipes.size() > 10 && ratings.size() > 20;
    }

    private void setRoleForUser(ApplicationUser user, Roles role) {
        Role newRole = roleRepository.findByName(role.name());
        List<Role> roles = user.getRoles();
        roles.add(newRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    private boolean hasUserTheRole(ApplicationUser user, Roles role) {
        List<Role> userRoles = user.getRoles();
        return userRoles.stream().anyMatch(r -> r.getName().equals(role.name()));
    }

    @Override
    public List<String> getBadgesOfCurrentUser() {
        List<String> roles = new ArrayList<>();
        ApplicationUser user = userManager.getCurrentUser();
        List<Role> userRoles = user.getRoles();
        for (int i = 0; i < Roles.values().length; i++) {
            if (userRoles.contains(roleRepository.findByName(Roles.values()[i].name()))) {
                roles.add(Roles.values()[i].name());
            }
            if (hasUserTheRole(user, Roles.StarCook)) {
                addRoleToUser(user, Roles.Cook);
                addRoleToUser(user, Roles.Contributor);
            }
        }
        return roles;
    }

    @Override
    public List<String> getBadgesOfUser(Long userId) {
        ApplicationUser user = userRepository.findFirstById(userId);
        List<Role> roles = user.getRoles();
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        return roleNames;
    }

}
