package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserPasswordChangeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.CustomUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomUserDetailServiceTest implements TestData {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

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

    @Test
    void getRecipesByUserIdReturnsRecipesWhenUserExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/1/recipes")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name", org.hamcrest.Matchers.is("Spagehtti plain")));
    }

    @Test
    void getRecipesByUserIdReturnsEmptyListWhenUserHasNoRecipes() throws Exception {
        mockMvc.perform(get("/api/v1/users/2/recipes")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getRecipesByUserIdReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/users/0/recipes")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void getRecipeBooksByUserIdReturnsRecipeBooksWhenUserExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/1/recipebooks")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(9)))
            .andExpect(jsonPath("$[0].name", org.hamcrest.Matchers.is("Italienische Küche")));
    }

    @Test
    void getRecipeBooksByUserIdReturnsEmptyListWhenUserHasNoRecipeBooks() throws Exception {
        mockMvc.perform(get("/api/v1/users/2/recipebooks")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getRecipeBooksByUserIdReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/users/0/recipebooks")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void getUserReturnsUserWhenUserExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/1/details")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", org.hamcrest.Matchers.is(1)));
    }

    @Test
    void getUserReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/users/0/details")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void changePasswordUpdatesPasswordForExistingUser() throws Exception {
        String jwttoken = LoginHelper();

        UserPasswordChangeDto userPasswordChangeDto = new UserPasswordChangeDto("password","newPassword");
        mockMvc.perform(patch("/api/v1/users/changePassword/1")
                .header(securityProperties.getAuthHeader(), jwttoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userPasswordChangeDto)))
            .andExpect(status().isOk());

    }

    @Test
    public void changePasswordReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        String jwttoken = LoginHelper();


        UserPasswordChangeDto userPasswordChangeDto = new UserPasswordChangeDto("oldPassword","newPassword");
        mockMvc.perform(patch("/api/v1/users/changePassword/999")
                .header(securityProperties.getAuthHeader(), jwttoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userPasswordChangeDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void changePasswordReturnsBadRequestWhenPasswordIsInvalid() throws Exception {
        String jwttoken = LoginHelper();

        UserPasswordChangeDto userPasswordChangeDto = new UserPasswordChangeDto("oldPassword","newPassword");
        mockMvc.perform(patch("/api/v1/users/changePassword/1")
                .header(securityProperties.getAuthHeader(), jwttoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userPasswordChangeDto)))
            .andExpect(status().isBadRequest());
    }

    private String LoginHelper() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("admin@email.com");
        userLoginDto.setPassword("password");

        String requestBody = objectMapper.writeValueAsString(userLoginDto);

        MvcResult mvcResult = mockMvc.perform(post(AUTH_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();
        return mvcResult.getResponse().getContentAsString();
    }


}