package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateForCreate(UserRegisterDto user) throws ValidationException, BadCredentialsException {
        LOG.trace("validateForCreate({})", user);
        List<String> validationErrors = new ArrayList<>();

        validateUser(user, validationErrors);

        if (userRepository.existsByUsername(user.username())) {
            validationErrors.add("Username already exists");
        }

        if (userRepository.existsByEmail(user.email())) {
            validationErrors.add("Email already exists");
        }

        if (!validationErrors.isEmpty()) {
            LOG.warn("Validation of User failed {}", validationErrors);
            throw new ValidationException("Validation of input fields failed", validationErrors);
        }
    }

    private void validateUser(UserRegisterDto user, List<String> validationErrors) throws BadCredentialsException {
        if (user == null) {
            validationErrors.add("User must not be null");
        } else {
            if (user.username() == null) {
                validationErrors.add("Username must not be null");
            } else {
                if (!user.username().matches("\\A\\p{ASCII}*\\z")) {
                    validationErrors.add("Username must only contain ASCII characters");
                }

                if (user.username().length() < 3 || user.username().length() > 255) {
                    validationErrors.add("Username must be between 3 and 255 characters");
                }
            }

            if (user.email() == null) {
                validationErrors.add("Email must not be null");
            } else {
                if (user.email().length() < 3 || user.email().length() > 255) {
                    validationErrors.add("Email must be between 3 and 255 characters");
                }

                if (user.email().contains(" ")) {
                    validationErrors.add("Email must not contain spaces");
                }

                if (!user.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    validationErrors.add("Email must be a valid email address");
                }
            }


            if (user.password() == null) {
                validationErrors.add("Password must not be null");
            } else {
                if (user.password().length() < 8 || user.password().length() > 255) {
                    validationErrors.add("Password must be between 8 and 255 characters");
                }

                if (user.password().contains(" ")) {
                    validationErrors.add("Password must not contain spaces");
                }

                if (user.password().contains(user.username())) {
                    validationErrors.add("Password must not contain the username");
                }

                if (user.password().contains(user.email())) {
                    validationErrors.add("Password must not contain the email");
                }

            }
        }
    }
}
