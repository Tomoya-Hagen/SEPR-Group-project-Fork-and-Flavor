package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDescriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.L;
import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.g;
import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.mg;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
@Transactional
class RecipeEndpointTest implements TestData {
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
        }
        ArrayList<IngredientDetailDto> actualIngredients = recipeDetailDto.ingredients();
        actualIngredients.sort(Comparator.comparing(IngredientDetailDto::id));
        Assertions.assertAll(
            () -> Assertions.assertEquals("Kartoffeln plain", recipeDetailDto.name()),
            () -> Assertions.assertEquals(
                1, recipeDetailDto.categories().size()),
            () -> Assertions.assertArrayEquals(new CategoryDetailDto[]{
                new CategoryDetailDto(3, "Beilage", "SIDE_DISH")}, recipeDetailDto.categories().toArray()),
            () -> Assertions.assertEquals(1, recipeDetailDto.ownerId()),
            () -> Assertions.assertEquals(4, recipeDetailDto.numberOfServings().intValue()),
            () -> Assertions.assertEquals(3, recipeDetailDto.recipeSteps().size()),
            () -> Assertions.assertEquals(0, recipeDetailDto.allergens().size()));
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
    void EndpointCheckCreateRecipeSimpleReturningLightRecipe() throws Exception {
        String jwttoken = LoginHelper();

        List<RecipeCategoryDto> recipeCategoryDtoList = new ArrayList<>();
        recipeCategoryDtoList.add(new RecipeCategoryDto(1));

        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(new RecipeIngredientDto(1,new BigDecimal(6),"g"));
        recipeIngredientDtos.add(new RecipeIngredientDto(132,new BigDecimal("12.5"),"g"));

        List<RecipeStepDto> recipeStepDtoList = new ArrayList<>();
        recipeStepDtoList.add(new RecipeStepDto("Step eins","Beschreibung von Step 1",0,true ));
        recipeStepDtoList.add(new RecipeStepDto("Step zwei","Beschreibung von Step 2",0,true ));

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
        recipeCreateDto.setName("Name");
        recipeCreateDto.setDescription("Beschreibung");
        recipeCreateDto.setServings((short)42);

        recipeCreateDto.setIngredients(recipeIngredientDtos);
        recipeCreateDto.setSteps(recipeStepDtoList);
        recipeCreateDto.setCategories(recipeCategoryDtoList);


        String requestBody = objectMapper.writeValueAsString(recipeCreateDto);

        MvcResult mvcResult = mockMvc.perform(post(RECIPE_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwttoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        DetailedRecipeDto lightrecipes = objectMapper.readValue(response.getContentAsString(),
            DetailedRecipeDto.class);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        Assertions.assertNotNull(lightrecipes);
        assertEquals(lightrecipes.getName(), recipeCreateDto.getName());
        assertEquals(lightrecipes.getDescription(), recipeCreateDto.getDescription());
    }


    @Test
    void EndpointCheckCreateRecipeSimpleWithoutJWT() throws Exception {

        List<RecipeCategoryDto> recipeCategoryDtoList = new ArrayList<>();
        recipeCategoryDtoList.add(new RecipeCategoryDto(1));

        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(new RecipeIngredientDto(1,new BigDecimal(6),"g"));
        recipeIngredientDtos.add(new RecipeIngredientDto(132,new BigDecimal(12.5),"g"));

        List<RecipeStepDto> recipeStepDtoList = new ArrayList<>();
        recipeStepDtoList.add(new RecipeStepDto("Step eins","Beschreibung von Step 1",0,true ));
        recipeStepDtoList.add(new RecipeStepDto("Step zwei","Beschreibung von Step 2",0,true ));

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
        recipeCreateDto.setName("Name");
        recipeCreateDto.setDescription("Beschreibung");
        recipeCreateDto.setServings((short)42);

        recipeCreateDto.setIngredients(recipeIngredientDtos);
        recipeCreateDto.setSteps(recipeStepDtoList);
        recipeCreateDto.setCategories(recipeCategoryDtoList);

        String requestBody = objectMapper.writeValueAsString(recipeCreateDto);

        MvcResult mvcResult = mockMvc.perform(post(RECIPE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isForbidden())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    void EndpointCheckCreateRecipeSimpleWithoutavailableCategory() throws Exception {
        String jwttoken = LoginHelper();

        List<RecipeCategoryDto> recipeCategoryDtoList = new ArrayList<>();
        recipeCategoryDtoList.add(new RecipeCategoryDto(1000));

        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(new RecipeIngredientDto(1,new BigDecimal(6),"g"));
        recipeIngredientDtos.add(new RecipeIngredientDto(132,new BigDecimal(12.5),"g"));

        List<RecipeStepDto> recipeStepDtoList = new ArrayList<>();
        recipeStepDtoList.add(new RecipeStepDto("Step eins","Beschreibung von Step 1",0,true ));
        recipeStepDtoList.add(new RecipeStepDto("Step zwei","Beschreibung von Step 2",0,true ));

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
        recipeCreateDto.setName("Name");
        recipeCreateDto.setDescription("Beschreibung");
        recipeCreateDto.setServings((short)42);

        recipeCreateDto.setIngredients(recipeIngredientDtos);
        recipeCreateDto.setSteps(recipeStepDtoList);
        recipeCreateDto.setCategories(recipeCategoryDtoList);


        String requestBody = objectMapper.writeValueAsString(recipeCreateDto);

        MvcResult mvcResult = mockMvc.perform(post(RECIPE_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwttoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void EndpointCheckCreateRecipeSimpleWithoutAnythingPossibleAvailable() throws Exception {
        String jwttoken = LoginHelper();

        List<RecipeCategoryDto> recipeCategoryDtoList = new ArrayList<>();
        recipeCategoryDtoList.add(new RecipeCategoryDto(1000));

        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(new RecipeIngredientDto(10000,new BigDecimal(6),"g"));

        List<RecipeStepDto> recipeStepDtoList = new ArrayList<>();
        recipeStepDtoList.add(new RecipeStepDto("Step eins",null,10000,true ));
        recipeStepDtoList.add(new RecipeStepDto("Step zwei",null,10000,false ));
        recipeStepDtoList.add(new RecipeStepDto("Step drei",null,-1,true ));
        recipeStepDtoList.add(new RecipeStepDto("Step vier",null,-1,false ));

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
        recipeCreateDto.setName("Name");
        recipeCreateDto.setDescription("Beschreibung");
        recipeCreateDto.setServings((short)42);

        recipeCreateDto.setIngredients(recipeIngredientDtos);
        recipeCreateDto.setSteps(recipeStepDtoList);
        recipeCreateDto.setCategories(recipeCategoryDtoList);


        String requestBody = objectMapper.writeValueAsString(recipeCreateDto);

        MvcResult mvcResult = mockMvc.perform(post(RECIPE_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwttoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        String[] requiredLines = {
            "Validation of Recipe to create failed.",
            "Category 1000 not found",
            "Ingredient 10000 not found",
            "Step Step eins is not valid",
            "Step Step zwei not found",
            "Step Step drei is not valid",
            "Step Step vier not found"
        };

        // Check each required line

        for (String requiredLine : requiredLines) {
            assertTrue(response.getContentAsString().contains(requiredLine));
        }
    }

    @Test
    void EndpointCheckCreateRecipeSimpleWithoutEachNeededInput() throws Exception {
        String jwttoken = LoginHelper();

        List<RecipeCategoryDto> recipeCategoryDtoList = new ArrayList<>();
        recipeCategoryDtoList.add(new RecipeCategoryDto(1));

        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(new RecipeIngredientDto(1,new BigDecimal(6),"g"));
        recipeIngredientDtos.add(new RecipeIngredientDto(132,new BigDecimal(12.5),"g"));

        List<RecipeStepDto> recipeStepDtoList = new ArrayList<>();
        recipeStepDtoList.add(new RecipeStepDto("Step eins","Beschreibung von Step 1",0,true ));
        recipeStepDtoList.add(new RecipeStepDto("Step zwei","Beschreibung von Step 2",0,true ));

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
        recipeCreateDto.setName("Name");
        recipeCreateDto.setDescription("Beschreibung");
        recipeCreateDto.setServings((short)42);

        recipeCreateDto.setIngredients(recipeIngredientDtos);
        recipeCreateDto.setSteps(recipeStepDtoList);
        recipeCreateDto.setCategories(recipeCategoryDtoList);

        RecipeCreateDto noname = new RecipeCreateDto(recipeCreateDto);
        noname.setName(null);
        RecipeCreateDto nodesc = new RecipeCreateDto(recipeCreateDto);
        nodesc.setDescription(null);
        RecipeCreateDto noservings = new RecipeCreateDto(recipeCreateDto);
        noservings.setServings(null);
        RecipeCreateDto noingredient = new RecipeCreateDto(recipeCreateDto);
        noingredient.setIngredients(null);
        RecipeCreateDto nocategory = new RecipeCreateDto(recipeCreateDto);
        nocategory.setCategories(null);
        RecipeCreateDto nosteps = new RecipeCreateDto(recipeCreateDto);
        nosteps.setSteps(null);

        MockHttpServletResponse nonameresponse = SimpleSend(jwttoken,noname,status().isBadRequest());
        assertEquals(HttpStatus.BAD_REQUEST.value(), nonameresponse.getStatus());

        MockHttpServletResponse nodescresponse = SimpleSend(jwttoken,nodesc,status().isBadRequest());
        assertEquals(HttpStatus.BAD_REQUEST.value(), nodescresponse.getStatus());

        MockHttpServletResponse noservingsresponse = SimpleSend(jwttoken,noservings,status().isBadRequest());
        assertEquals(HttpStatus.BAD_REQUEST.value(), noservingsresponse.getStatus());

        MockHttpServletResponse noingredientresponse = SimpleSend(jwttoken,noingredient,status().isBadRequest());
        assertEquals(HttpStatus.BAD_REQUEST.value(), noingredientresponse.getStatus());

        MockHttpServletResponse nocategoryresponse = SimpleSend(jwttoken,nocategory,status().isBadRequest());
        assertEquals(HttpStatus.BAD_REQUEST.value(), nocategoryresponse.getStatus());

        MockHttpServletResponse nostepsresponse = SimpleSend(jwttoken,nosteps,status().isBadRequest());
        assertEquals(HttpStatus.BAD_REQUEST.value(), nostepsresponse.getStatus());
    }

    private MockHttpServletResponse SimpleSend(String jwttoken, Object obj, ResultMatcher matcher) throws Exception {
        String requestBody = objectMapper.writeValueAsString(obj);

        MvcResult mvcResult = mockMvc.perform(post(RECIPE_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwttoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(matcher)
            .andReturn();
        return mvcResult.getResponse();
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

    @Test
    void searchRecipeReturnsEmptyListNotFound() throws Exception {
        mockMvc
            .perform(get("/api/v1/recipes") // Corrected endpoint
                .param("name", "Gurke")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(0))); // Update to reflect the structure of the returned Page<RecipeListDto>
    }

    @Test
    void searchRecipeReturnsFoundRecipe() throws Exception {
        mockMvc.perform(get("/api/v1/recipes")
                .param("name", "Apfelkuchen nach Ing")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1))) // Update to reflect the structure of the returned Page<RecipeListDto>
            .andExpect(jsonPath("$.content[0].name", is("Apfelkuchen nach Ing"))); // Update to reflect the structure of the returned RecipeListDto
    }
}
