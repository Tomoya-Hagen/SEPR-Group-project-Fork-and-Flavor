package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDescriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.L;
import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.g;
import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.mg;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void ReturnARecipeDetailDtoIfARecipeExistsByRecipeId() throws Exception {
        ArrayList<IngredientDetailDto> ingredientDetailDtos = new java.util.ArrayList<>(List.of(
            new IngredientDetailDto(5, "Basmatireis", new BigDecimal("1.00"), mg.name()),
            new IngredientDetailDto(133, "Salz", new BigDecimal("2.00"), L.name()),
            new IngredientDetailDto(189, "Zwiebel", new BigDecimal("3.00"), mg.name()),
            new IngredientDetailDto(23, "Ei", new BigDecimal("4.00"), g.name()),
            new IngredientDetailDto(68, "Knoblauch", new BigDecimal("5.00"), g.name()),
            new IngredientDetailDto(58, "Jungzwiebel", new BigDecimal("6.00"), mg.name()),
            new IngredientDetailDto(147, "SesamÃ¶l", new BigDecimal("7.00"), g.name())));
        ingredientDetailDtos.sort(Comparator.comparing(IngredientDetailDto::id));
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_BASE_URI + "/details/{id}", 2))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        RecipeDetailDto recipeDetailDto = objectMapper.readValue(response.getContentAsString(),
            RecipeDetailDto.class);

        Assertions.assertNotNull(recipeDetailDto);
        for (int i = 0; i < recipeDetailDto.recipeSteps().size(); i++) {
            if (recipeDetailDto.recipeSteps().get(i) instanceof RecipeStepDescriptionDetailDto recipeStepDescriptionDetailDto) {
                recipeStepDescriptionDetailDto.setDescription(String.valueOf(StandardCharsets.ISO_8859_1.encode(recipeStepDescriptionDetailDto.getDescription())));
            }
            ;
        }
        ArrayList<IngredientDetailDto> actualIngredients = recipeDetailDto.ingredients();
        actualIngredients.sort(Comparator.comparing(IngredientDetailDto::id));
        Assertions.assertAll(
            () -> Assertions.assertEquals("Egg Fried Rice", recipeDetailDto.name()),
            () -> Assertions.assertEquals(
                1, recipeDetailDto.categories().size()),
            () -> Assertions.assertArrayEquals(new CategoryDetailDto[]{
                new CategoryDetailDto(1, "Hautpspeise", "MAIN_COURSE")}, recipeDetailDto.categories().toArray()),
            () -> Assertions.assertEquals(1, recipeDetailDto.ownerId()),
            () -> Assertions.assertEquals("Ein schnelles asiatisches Gericht.", recipeDetailDto.description()),
            () -> Assertions.assertEquals(1, recipeDetailDto.numberOfServings().intValue()),
            () -> Assertions.assertArrayEquals(ingredientDetailDtos.toArray()
                , actualIngredients.toArray()),
            () -> Assertions.assertEquals(5, recipeDetailDto.recipeSteps().size()),
            () -> Assertions.assertEquals(2, recipeDetailDto.allergens().size()),
            () -> Assertions.assertEquals(new BigDecimal("1366.40"), recipeDetailDto.nutritions().stream().filter(n -> n.id() == 1).findFirst().get().value()));
    }

    @Test
    void Return404StatusCodeIfNoRecipeWasFoundByTheGivenId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_BASE_URI + "/details/{id}", -1)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void ReturnAListOfOneRecipeListDtoFromGetAllFromPageOneWithStepOne() throws Exception {
        List<RecipeListDto> expectedRecipeListDtos = List.of(
            new RecipeListDto(1, "Reis", "So muss Reis schmecken!", 0));
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_BASE_URI + "/?page=1&step=1"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        List<RecipeListDto> recipes = Arrays.stream(objectMapper.readValue(response.getContentAsString(),
            RecipeListDto[].class)).toList();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        Assertions.assertEquals(1, recipes.size());
        Assertions.assertEquals(expectedRecipeListDtos, recipes);
    }

    @Test
    void ReturnAListOfTwoRecipeListDtoFromGetAllFromPageOneWithStepThree() throws Exception {
        List<RecipeListDto> expectedRecipeListDtos = List.of(
            new RecipeListDto(1, "Reis", "So muss Reis schmecken!", 0),
            new RecipeListDto(2, "Egg Fried Rice", "Ein schnelles asiatisches Gericht.", 0));
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_BASE_URI + "/?page=1&step=3"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        List<RecipeListDto> recipes = Arrays.stream(objectMapper.readValue(response.getContentAsString(),
            RecipeListDto[].class)).toList();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        Assertions.assertEquals(2, recipes.size());
        Assertions.assertEquals(expectedRecipeListDtos, recipes);
    }

    @Test
    void ReturnAnEmptyListOfRecipeListDtoFromGetAllFromPageTwoWithStepTwo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_BASE_URI + "/?page=2&step=2"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        List<RecipeListDto> recipes = Arrays.stream(objectMapper.readValue(response.getContentAsString(),
            RecipeListDto[].class)).toList();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        Assertions.assertTrue(recipes.isEmpty());
    }

    @Test
    public void searchTournamentReturns404() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/recipes/")
                .queryParam("name", "Gurke")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn().getResponse().getContentAsByteArray();
    }



}
