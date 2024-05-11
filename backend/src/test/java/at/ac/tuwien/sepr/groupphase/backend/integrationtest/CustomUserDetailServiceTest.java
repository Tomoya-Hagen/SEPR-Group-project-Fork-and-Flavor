package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.EmailException;
import at.ac.tuwien.sepr.groupphase.backend.exception.UsernameException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.CustomUserDetailService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
@ActiveProfiles("generateData")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomUserDetailServiceTest implements TestData {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataGenerator userDataGenerator;

    @BeforeEach
    void before() {
        userDataGenerator.generateTestData();
    }

    @AfterEach
    void after() {
    }

    @Test
    public void registerValidUser() throws Exception {
        UserRegisterDto validUser = new UserRegisterDto();
        validUser.setEmail("validuser@email.com");
        validUser.setPassword("password");
        validUser.setUsername("validuser");

        String response = customUserDetailService.register(validUser);

        assertAll(
            () -> assertNotNull(response),
            () -> assertTrue(response.contains("Bearer")),
            () -> assertTrue(userRepository.existsByEmail(validUser.getEmail())),
            () -> assertTrue(userRepository.existsByUsername(validUser.getUsername()))
        );
    }

    @Test
    public void registerInvalidUserEmail() throws Exception {
        UserRegisterDto invalidUser = new UserRegisterDto();
        invalidUser.setEmail("invalidEmail");
        invalidUser.setPassword("password");
        invalidUser.setUsername("invaliduser");

        assertThrowsExactly(EmailException.class, () -> customUserDetailService.register(invalidUser));
    }


    @Test
    public void registerUserWithDuplicateEmail() throws Exception {
        UserRegisterDto invalidUser = new UserRegisterDto();
        invalidUser.setEmail("admin@email.com");
        invalidUser.setPassword("password");
        invalidUser.setUsername("invaliduser");

        assertThrowsExactly(EmailException.class, () -> customUserDetailService.register(invalidUser));
    }

    @Test
    public void registerUserWithDuplicateUsername() throws Exception {
        UserRegisterDto invalidUser = new UserRegisterDto();
        invalidUser.setEmail("administrator@email.com");
        invalidUser.setPassword("password");
        invalidUser.setUsername("admin");

        assertThrowsExactly(UsernameException.class, () -> customUserDetailService.register(invalidUser));
    }


}