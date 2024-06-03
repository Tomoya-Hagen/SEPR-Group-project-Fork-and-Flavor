package at.ac.tuwien.sepr.groupphase.backend.service.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, UserRegisterDto> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserRegisterDto user, ConstraintValidatorContext context) {
        if (user == null || user.password() == null) {
            return false;
        }

        boolean isValid = true;

        String password = user.password();

        if (password.contains(" ")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password must not contain spaces")
                .addPropertyNode("password").addConstraintViolation();
            isValid = false;
        }

        if (password.contains(user.username())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password must not contain the username")
                .addPropertyNode("password").addConstraintViolation();
            isValid = false;
        }

        if (password.contains(user.email())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password must not contain the email")
                .addPropertyNode("password").addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
