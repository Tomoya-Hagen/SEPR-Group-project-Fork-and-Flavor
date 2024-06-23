package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RatingEndpointTest implements TestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;
    @Test
    void GetRatingsByRecipeIdReturnNotFoundIfRecipeDoesNotExist() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RATING_BASE_URI + "/recipe/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void GetRatingsByRecipeId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RATING_BASE_URI + "/recipe/1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        RatingListDto[] ratings = objectMapper.readValue(response.getContentAsString(),
            RatingListDto[].class);
        Assertions.assertNotNull(ratings);
        assertEquals(4, ratings.length);
    }

    @Test
    void CreateNewRatingForRecipe() throws Exception {
        RatingCreateDto ratingCreateDto = new RatingCreateDto(1,2,3,4,"test review");
        MvcResult mvcResult = this.mockMvc.perform(post(RATING_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingCreateDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        RatingListDto newRating = objectMapper.readValue(response.getContentAsString(),
            RatingListDto.class);
        Assertions.assertNotNull(newRating);
        Assertions.assertAll(
            () -> Assertions.assertEquals(ratingCreateDto.cost(), newRating.cost()),
            () -> Assertions.assertEquals(ratingCreateDto.recipeId(), newRating.recipeId()),
            () -> Assertions.assertEquals(ratingCreateDto.review(), newRating.review()),
            () -> Assertions.assertEquals(ratingCreateDto.easeOfPrep(), newRating.easeOfPrep()),
            () -> Assertions.assertEquals(ratingCreateDto.taste(), newRating.taste()),
            () -> Assertions.assertEquals(1, newRating.user().id())
        );
    }

    @Test
    void CreateNewRatingReturnsNotFoundExceptionIfRecipeDoesNotExist() throws Exception {
        RatingCreateDto ratingCreateDto = new RatingCreateDto(-1,2,3,4,"test review");
        MvcResult mvcResult = this.mockMvc.perform(post(RATING_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingCreateDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void CreateNewRatingReturnsDuplicateObjectExceptionIfARatingFromANUserToARecipeAlreadyExists() throws Exception {
        RatingCreateDto ratingCreateDto = new RatingCreateDto(2,2,3,4,"test review");
        MvcResult mvcResult = this.mockMvc.perform(post(RATING_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingCreateDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("user@email.com", ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getRatingFromUserByIdReturnsRatingsWhenUserExists() throws Exception {
        long userId = 2L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ratings/user/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10));
    }

    @Test
    void getRatingFromUserByIdThrowsNotFoundExceptionWhenUserDoesNotExist() throws Exception {
        long userId = 9999L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ratings/user/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
