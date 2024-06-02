package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.CustomUserDetailService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.annotation.DirtiesContext;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Disabled
class CustomUserDetailServiceTest implements TestData {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerValidUser() throws ValidationException {
        UserRegisterDto validUser = new UserRegisterDto(
            "validuser@email.com",
            "password",
            "validuser"
        );

        String response = customUserDetailService.register(validUser);

        assertAll(
            () -> assertNotNull(response),
            () -> assertTrue(response.contains("Bearer")),
            () -> assertTrue(userRepository.existsByEmail(validUser.email())),
            () -> assertTrue(userRepository.existsByUsername(validUser.username()))
        );
    }

    @Test
    void registerInvalidUserEmail() {
        UserRegisterDto invalidUser = new UserRegisterDto(
            "invalidEmail",
            "password",
            "invaliduser"
        );

        Exception exception = assertThrowsExactly(ValidationException.class, () -> customUserDetailService.register(invalidUser));
        String expectedMessage = "Email must be a valid email address";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void registerUserWithDuplicateEmail() {
        UserRegisterDto invalidUser = new UserRegisterDto(
            "admin@email.com",
            "password",
            "invaliduser"
        );

        Exception exception = assertThrowsExactly(ValidationException.class, () -> customUserDetailService.register(invalidUser));
        String expectedMessage = "Email already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void registerUserWithDuplicateUsername() {
        UserRegisterDto invalidUser = new UserRegisterDto(
            "administrator@email.com",
            "password",
            "admin"
        );

        Exception exception = assertThrowsExactly(ValidationException.class, () -> customUserDetailService.register(invalidUser));
        String expectedMessage = "Username already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


}