package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginEndpointTest implements TestData {

    private final static String ENDPOINT = "/api/v1/authentication";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerValidUser() throws Exception {
        UserRegisterDto validUser = new UserRegisterDto(
            "validuser@email.com",
            "password",
            "validuser"
        );

        MvcResult mvcResult = this.mockMvc.perform(post(ENDPOINT+"/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validUser)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertNotNull(response),
            () -> assertTrue(response.getContentAsString().contains("Bearer")),
            () -> assertTrue(userRepository.existsByEmail(validUser.email())),
            () -> assertTrue(userRepository.existsByUsername(validUser.username()))
        );
    }

    @Test
    void registerInvalidUserEmail() throws Exception {
        UserRegisterDto invalidUser = new UserRegisterDto(
            "invalidEmail",
            "password",
            "invaliduser"
        );

        mockMvc.perform(post(ENDPOINT + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    void registerUserWithDuplicateEmail() throws Exception {
        ApplicationUser user1 = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withEmail("duplicate@email.com")
            .withPassword("password")
            .withUsername("user1")
            .build();
        userRepository.save(user1);
        userRepository.flush();

        UserRegisterDto user2 = new UserRegisterDto(
            "duplicate@email.com",
            "password2",
            "user2"
        );

        mockMvc.perform(post(ENDPOINT + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    void registerUserWithDuplicateUsername() throws Exception {
        ApplicationUser user1 = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withEmail("adsfaew@email.com")
            .withPassword("password")
            .withUsername("user1")
            .build();
        userRepository.save(user1);
        userRepository.flush();

        UserRegisterDto user2 = new UserRegisterDto(
            "af4w23wr@email.com",
            "password2",
            "user1"
        );

        mockMvc.perform(post(ENDPOINT + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

}
