package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepr.groupphase.backend.service.Roles;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.RatingServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.L;
import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.g;
import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.mg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RecipeServiceTest implements TestData {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RatingServiceImpl ratingServiceImpl;
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Test
    void ReturnARecipeDetailDtoIfARecipeWithTheGivenRecipeIdExists() {
        long recipeId = 2;
        ArrayList<IngredientDetailDto> ingredientDetailDtos = new java.util.ArrayList<>(List.of(
            new IngredientDetailDto(5, "Basmatireis", new BigDecimal("1.00"), mg.name()),
            new IngredientDetailDto(133, "Salz", new BigDecimal("2.00"), L.name()),
            new IngredientDetailDto(189, "Zwiebel", new BigDecimal("3.00"), mg.name()),
            new IngredientDetailDto(23, "Ei", new BigDecimal("4.00"), g.name()),
            new IngredientDetailDto(68, "Knoblauch", new BigDecimal("5.00"), g.name()),
            new IngredientDetailDto(58, "Jungzwiebel", new BigDecimal("6.00"), mg.name()),
            new IngredientDetailDto(147, "Sesamöl", new BigDecimal("7.00"), g.name())));
        ingredientDetailDtos.sort(Comparator.comparing(IngredientDetailDto::id));
        RecipeDetailDto recipe = recipeService.getRecipeDetailDtoById(recipeId);
        List<IngredientDetailDto> actualIngredients = recipe.ingredients();
        Assertions.assertNotNull(recipe);
        Assertions.assertAll(
            () -> Assertions.assertEquals("Kartoffeln plain", recipe.name()),
            () -> Assertions.assertEquals(
                1, recipe.categories().size()),
            () -> Assertions.assertArrayEquals(new CategoryDetailDto[]{
                new CategoryDetailDto(3, "Beilage", "SIDE_DISH")}, recipe.categories().toArray()),
            () -> Assertions.assertEquals(1, recipe.ownerId()),
            () -> Assertions.assertEquals("Unterrezept für Kartoffelgerichte", recipe.description()),
            () -> Assertions.assertEquals(4, recipe.numberOfServings().intValue()),
            () -> Assertions.assertEquals(3, recipe.recipeSteps().size()),
            () -> Assertions.assertEquals(0, recipe.allergens().size()));
    }

    @Test
    void ReturnANotFoundExceptionIfNoRecipeWasFoundWithTheGivenId() {
        long recipeId = 2000;
        Assertions.assertThrows(NotFoundException.class, () -> recipeService.getRecipeDetailDtoById(recipeId));
    }

    @Test
    void ReturnAListOfOneRecipeListDtoFromGetAllFromPageOneWithStepOne() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<RecipeListDto> recipes = recipeService.getRecipesByName("Spagehtti plain", pageable);
        assertEquals(1, recipes.getTotalElements());
        assertEquals("Spagehtti plain", recipes.getContent().get(0).name());
    }

    @Test
    void ReturnAnEmptyListOfRecipeListDtoFromGetAllFromPageTwohundredWithStepTwohundred() {
        Pageable pageable = PageRequest.of(0, 200);
        Page<RecipeListDto> recipes = recipeService.getRecipesByName("Nonexistent", pageable);
        Assertions.assertTrue(recipes.isEmpty());
    }

    @Test
    void CreateRecipeShouldCreateRecipePlusDependencies() throws Exception {
        userAuthenticationByEmail("admin@email.com");
        List<RecipeCategoryDto> recipeCategoryDtoList = new ArrayList<>();
        recipeCategoryDtoList.add(new RecipeCategoryDto(1));

        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(new RecipeIngredientDto(1, new BigDecimal(6), "g"));
        recipeIngredientDtos.add(new RecipeIngredientDto(132, new BigDecimal(12.5), "g"));

        List<RecipeStepDto> recipeStepDtoList = new ArrayList<>();
        recipeStepDtoList.add(new RecipeStepDto("Step eins", "Beschreibung von Step 1", 0, true));
        recipeStepDtoList.add(new RecipeStepDto("Step zwei", "Beschreibung von Step 2", 0, true));

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
        recipeCreateDto.setName("Name");
        recipeCreateDto.setDescription("Beschreibung");
        recipeCreateDto.setNumberOfServings((short) 42);

        recipeCreateDto.setIngredients(recipeIngredientDtos);
        recipeCreateDto.setRecipeSteps(recipeStepDtoList);
        recipeCreateDto.setCategories(recipeCategoryDtoList);

        DetailedRecipeDto ret = recipeService.createRecipe(recipeCreateDto);
        Assertions.assertNotNull(ret);
        Assertions.assertEquals("Beschreibung", ret.getDescription());
        Assertions.assertEquals("Name", ret.getName());
        long retid = ret.getId();

        Recipe recipefDB = recipeRepository.getRecipeById(retid).get();
        Assertions.assertNotNull(recipefDB);

        Assertions.assertEquals(recipefDB.getName(), recipeCreateDto.getName());
        Assertions.assertEquals(recipefDB.getDescription(), recipeCreateDto.getDescription());
        Assertions.assertEquals(recipefDB.getNumberOfServings(), recipeCreateDto.getNumberOfServings());

        Assertions.assertTrue(
            IntStream.range(0, recipeIngredientDtos.size())
                .allMatch(i -> recipefDB.getIngredients().get(i).getIngredient().getId() == recipeIngredientDtos.get(i).getId()));
        Assertions.assertTrue(
            IntStream.range(0, recipeStepDtoList.size())
                .allMatch(i -> Objects.equals(recipefDB.getRecipeSteps().get(i).getName(), recipeStepDtoList.get(i).getName())));
        Assertions.assertTrue(
            IntStream.range(0, recipeCategoryDtoList.size())
                .allMatch(i -> recipefDB.getCategories().get(i).getId() == recipeCategoryDtoList.get(i).getId()));
    }

    @Test
    void searchByNameShouldFind() {
        Pageable pageable = PageRequest.of(0, 10);
        var recipePage = recipeService.getRecipesByName("Apfelkuchen", pageable);
        assertNotNull(recipePage);
        assertThat(recipePage.getContent()).hasSize(2);
        assertEquals("Apfelkuchen", recipePage.getContent().get(0).name());
    }

    @Test
    void searchByNameShouldNotFind() {
        Pageable pageable = PageRequest.of(0, 10);
        var recipePage = recipeService.getRecipesByName("Gurke", pageable);
        Assertions.assertTrue(recipePage.isEmpty());

        recipePage = recipeService.getRecipesByName("Kaschew", pageable);
        Assertions.assertTrue(recipePage.isEmpty());
    }

    @Test
    void verifyingRecipeRecipeNotByAStarCookThrowsError() {
        userAuthenticationByEmail("user@email.com");
        Recipe recipe = recipeRepository.findById(1L);
        Assertions.assertTrue(recipe.getVerifiers().isEmpty());
        ApplicationUser user = userRepository.findById(2L).get();
        Assertions.assertFalse(user.getRoles().contains(roleRepository.findByName(Roles.StarCook.name())));
        Assertions.assertThrows(ForbiddenException.class, () -> recipeService.verifyRecipe(1L));
    }

    @Test
    void verifyFindBestRecipeBy() throws ValidationException {
        Random r = new Random();
        userAuthenticationByEmail("user@email.com");

        List<RecipeCategoryDto> recipeCategoryDtoList = new ArrayList<>();
        recipeCategoryDtoList.add(new RecipeCategoryDto(1));

        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(new RecipeIngredientDto(1, new BigDecimal(6), "g"));
        recipeIngredientDtos.add(new RecipeIngredientDto(132, new BigDecimal(12.5), "g"));

        List<RecipeStepDto> recipeStepDtoList = new ArrayList<>();
        recipeStepDtoList.add(new RecipeStepDto("Step eins", "Beschreibung von Step 1", 0, true));
        recipeStepDtoList.add(new RecipeStepDto("Step zwei", "Beschreibung von Step 2", 0, true));

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
        recipeCreateDto.setName("TEST REZEPT WAS ALLES KANN");
        recipeCreateDto.setDescription("Beschreibung");
        recipeCreateDto.setNumberOfServings((short) 42);

        recipeCreateDto.setIngredients(recipeIngredientDtos);
        recipeCreateDto.setRecipeSteps(recipeStepDtoList);
        recipeCreateDto.setCategories(recipeCategoryDtoList);

        var recipe = recipeService.createRecipe(recipeCreateDto);
        long recipeid = recipe.getId();

        for(int i = 0; i <= 10; i++){
            simpleNewRate(r.nextInt(),recipeid);
        }


        Page<RecipeListDto> res = recipeService.byBest(5);
        RecipeListDto searched = recipeService.getRecipesByName("TEST REZEPT WAS ALLES KANN",PageRequest.of(0,1)).getContent().getFirst();
        assertTrue(res.getContent().contains(searched));



    }

    private void simpleNewRate(int user, long id) throws ValidationException {
        String email = user + "@mail.com";
        UserRegisterDto userRegisterDto = new UserRegisterDto(email,"password",user + "");
        customUserDetailService.register(userRegisterDto);
        userAuthenticationByEmail(email);
        RatingCreateDto ratin = new RatingCreateDto(id,5,5,5,"BESTE");
        ratingServiceImpl.createRating(ratin);
    }


}
