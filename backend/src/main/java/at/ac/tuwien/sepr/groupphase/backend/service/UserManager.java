package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class UserManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApplicationUser getCurrentUser() {
        LOGGER.trace("getCurrentUser()");
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new ForbiddenException("no user is currently logged in");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("the logged-in user was not found in the system");
        }
        return userRepository.findFirstUserByEmail(email);
    }
}
