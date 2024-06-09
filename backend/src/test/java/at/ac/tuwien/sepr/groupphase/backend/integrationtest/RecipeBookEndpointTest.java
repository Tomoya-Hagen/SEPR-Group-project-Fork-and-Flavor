package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeBookMapper;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RecipeBookEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void searchRecipeBooksReturnsRecipeBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipebook")
                .param("name", "Italienische Küche")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Italienische Küche"));
    }

    @Test
    void searchRecipeBooksReturnsEmptyListWhenNoMatch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipebook")
                .param("name", "Nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty());
    }

    @Test
    void getNonExistingIdReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/999/details"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getListByPageAndStepReturnsRecipeBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipebook")
                .param("name", "")
                .param("page", "0")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Italienische Küche"));
    }

    @Test
    void getListByPageAndStepReturnsBadRequestWhenPageIsNotNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipebook")
                .param("name", "")
                .param("page", "notANumber")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getListByPageAndStepReturnsBadRequestWhenStepIsNotNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipebook")
                .param("name", "")
                .param("page", "1")
                .param("size", "notANumber")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getRecipeBookListReturnsRecipeBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipebook")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(9));
    }

    @Test
    void serviceShouldThrowANotFoundExceptionIfARecipeIsAddedToARecipeBookThatDoesNotExist() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(patch(RECIPE_BOOK_BASE_URI + "/16/spoon/-3")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void serviceShouldThrowANotFoundExceptionIfARecipeBookDoesNotExist() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(patch(RECIPE_BOOK_BASE_URI + "/20/spoon/1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void serviceShouldAddARecipeToARecipeBook() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(patch(RECIPE_BOOK_BASE_URI + "/7/spoon/2")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        RecipeBookDetailDto recipeBookDetailDto = objectMapper.readValue(response.getContentAsString(),
            RecipeBookDetailDto.class);
        assertEquals(1, recipeBookDetailDto.recipes().stream().filter(r -> r.id() == 2L).count());
    }

    @Test
    void serviceShouldThrowAnMethodForbiddenExceptionIfTheUserIsNotTheOwnerOrInTheUsersListOfAnRecipeBook() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(patch(RECIPE_BOOK_BASE_URI + "/7/spoon/2")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("contributor@email.com", ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    void serviceShouldReturnAllRecipeBooksThatAnUserHasWriteRightsFor() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_BOOK_BASE_URI + "/user")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<RecipeBookListDto> recipeBookListDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            RecipeBookListDto[].class));
        Assertions.assertAll(
            () -> Assertions.assertFalse(recipeBookListDtos.isEmpty()),
            () -> assertEquals(9, recipeBookListDtos.size())
        );
    }
}
