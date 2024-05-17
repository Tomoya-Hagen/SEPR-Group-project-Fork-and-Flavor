package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import at.ac.tuwien.sepr.groupphase.backend.exception.EmailException;
import at.ac.tuwien.sepr.groupphase.backend.exception.PasswordException;
import at.ac.tuwien.sepr.groupphase.backend.exception.UsernameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;

import java.lang.invoke.MethodHandles;

@Component
public class UserValidator {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void validateForCreate(UserRegisterDto user) throws EmailException, PasswordException, UsernameException, BadCredentialsException {
        LOG.trace("validateForCreate({})", user);

        validateUser(user);

    }

    private void validateUser(UserRegisterDto user) throws EmailException, PasswordException, UsernameException, BadCredentialsException {
        if (user == null) {
            throw new BadCredentialsException("User must not be null");
        }

        if (user.username() == null) {
            throw new UsernameException("Username must not be null");
        }

        if (!user.username().matches("\\A\\p{ASCII}*\\z")) {
            throw new UsernameException("Username must only contain ASCII characters");
        }

        if (user.username().length() < 3 || user.username().length() > 255) {
            throw new UsernameException("Username must be between 3 and 255 characters");
        }

        if (user.email() == null) {
            throw new EmailException("Email must not be null");
        }

        if (user.email().length() < 3 || user.email().length() > 255) {
            throw new EmailException("Email must be between 3 and 255 characters");
        }

        if (user.email().contains(" ")) {
            throw new EmailException("Email must not contain spaces");
        }

        if (!user.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new EmailException("Email must be a valid email address");
        }

        if (user.password() == null) {
            throw new PasswordException("Password must not be null");
        }

        if (user.password().length() < 8 || user.password().length() > 255) {
            throw new PasswordException("Password must be between 8 and 255 characters");
        }

        if (user.password().contains(" ")) {
            throw new PasswordException("Password must not contain spaces");
        }

        /* if (!user.password().matches(".*[0-9].*")) {
            throw new PasswordException("Password must contain at least one number");
        }

        if (!user.password().matches(".*[a-z].*")) {
            throw new PasswordException("Password must contain at least one lowercase letter");
        }

        if (!user.password().matches(".*[A-Z].*")) {
            throw new PasswordException("Password must contain at least one uppercase letter");
        }

        if (!user.password().matches(".*[!@#$%^&*].*")) {
            throw new PasswordException("Password must contain at least one special character");
        }*/

        if (user.password().contains(user.username())) {
            throw new PasswordException("Password must not contain the username");
        }

        if (user.password().contains(user.email())) {
            throw new PasswordException("Password must not contain the email");
        }
    }
}
