package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserPasswordChangeDto;
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

    public void validateForPasswordChange(UserPasswordChangeDto userPasswordChangeDto) throws ValidationException {
        LOG.trace("validateForPasswordChange({})", userPasswordChangeDto);
        List<String> validationErrors = new ArrayList<>();

        if (userPasswordChangeDto.newPassword().isEmpty()) {
            validationErrors.add("Password cannot be empty");
        }

        if (userPasswordChangeDto.newPassword().length() < 8) {
            validationErrors.add("Password needs to be longer then 8 characters");
        }

        if (!validationErrors.isEmpty()) {
            LOG.warn("Validation of password change failed {}", validationErrors);
            throw new ValidationException("Validation of password change failed", validationErrors);
        }
    }
}
