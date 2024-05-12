package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateData"})
@AutoConfigureMockMvc
class RecipeEndpointShould implements TestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void ReturnARecipeDetailDtoIfARecipeExistsByRecipeId() throws Exception {
        List<Recipe> recipes = recipeRepository.findAll();
        ArrayList<IngredientDetailDto> ingredientDetailDtos = new java.util.ArrayList<>(List.of(
            new IngredientDetailDto(5, "Basmatireis", new BigDecimal("1.00"), 1),
            new IngredientDetailDto(133, "Salz", new BigDecimal("2.00"), 1),
            new IngredientDetailDto(189, "Zwiebel", new BigDecimal("3.00"), 1),
            new IngredientDetailDto(23, "Ei", new BigDecimal("4.00"), 1),
            new IngredientDetailDto(68, "Knoblauch", new BigDecimal("5.00"), 1),
            new IngredientDetailDto(58, "Jungzwiebel", new BigDecimal("6.00"), 1),
            new IngredientDetailDto(147, "Sesamöl", new BigDecimal("7.00"), 1)));

        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_BASE_URI+"/{id}",2)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,true);
        RecipeDetailDto recipeDetailDto = objectMapper.readValue(response.getContentAsString(),
            RecipeDetailDto.class);

        Assertions.assertNotNull(recipeDetailDto);
        ArrayList<IngredientDetailDto> actualIngredients = recipeDetailDto.ingredients();
        actualIngredients.sort(Comparator.comparing(IngredientDetailDto::id));
        Assertions.assertAll(
            () -> Assertions.assertEquals("Egg Fried Rice", recipeDetailDto.name()),
            () -> Assertions.assertEquals(
                1, recipeDetailDto.categories().size()),
            () -> Assertions.assertArrayEquals(new CategoryDetailDto[]{
                new CategoryDetailDto(2, "Hautpspeise", "MAIN_COURSE")}, recipeDetailDto.categories().toArray()),
            () -> Assertions.assertEquals(1, recipeDetailDto.ownerId()),
            () -> Assertions.assertEquals("Ein schnelles asiatisches Gericht.", recipeDetailDto.description()),
            () -> Assertions.assertEquals(1, recipeDetailDto.numberOfServings().intValue()),
            () -> Assertions.assertArrayEquals(ingredientDetailDtos.toArray()
                , actualIngredients.toArray()),
            () -> Assertions.assertEquals(5, recipeDetailDto.recipeSteps().size()),
            () -> Assertions.assertEquals(2, recipeDetailDto.allergens().size()),
            () -> Assertions.assertEquals(new BigDecimal("1366.40"),recipeDetailDto.nutritions().stream().filter(n->n.id() == 1).findFirst().get().value()));
    }

    @Test
    void Return404StatusCodeIfNoRecipeWasFoundByTheGivenId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_BASE_URI + "/{id}", -1)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}
