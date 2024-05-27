package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.L;
import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.g;
import static at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient.Unit.mg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles({"test", "generateData"})
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RecipeServiceShould {
    @Autowired
    private RecipeService recipeService;

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
        ArrayList<IngredientDetailDto> actualIngredients = recipe.ingredients();
        actualIngredients.sort(Comparator.comparing(IngredientDetailDto::id));
        Assertions.assertNotNull(recipe);
        Assertions.assertAll(
            () -> Assertions.assertEquals("Egg Fried Rice", recipe.name()),
            () -> Assertions.assertEquals(
                1, recipe.categories().size()),
            () -> Assertions.assertArrayEquals(new CategoryDetailDto[]{
                new CategoryDetailDto(1, "Hautpspeise", "MAIN_COURSE")}, recipe.categories().toArray()),
            () -> Assertions.assertEquals(1, recipe.ownerId()),
            () -> Assertions.assertEquals("Ein schnelles asiatisches Gericht.", recipe.description()),
            () -> Assertions.assertEquals(1, recipe.numberOfServings().intValue()),
            () -> Assertions.assertArrayEquals(ingredientDetailDtos.toArray()
                , actualIngredients.toArray()),
            () -> Assertions.assertEquals(5, recipe.recipeSteps().size()),
            () -> Assertions.assertEquals(2, recipe.allergens().size()),
            () -> Assertions.assertEquals(new BigDecimal("1366.40"), recipe.nutritions().stream().filter(n -> n.id() == 1).findFirst().get().value()));
    }

    @Test
    void ReturnANotFoundExceptionIfNoRecipeWasFoundWithTheGivenId() {
        long recipeId = 2000;
        Assertions.assertThrows(NotFoundException.class, () -> recipeService.getRecipeDetailDtoById(recipeId));
    }

    @Test
    void ReturnAListOfOneRecipeListDtoFromGetAllFromPageOneWithStepOne() {
        List<RecipeListDto> expectedRecipeListDtos = List.of(
            new RecipeListDto(1, "Reis", "So muss Reis schmecken!", 0));
        List<RecipeListDto> recipes = recipeService.getRecipesFromPageInSteps(1, 1);
        Assertions.assertEquals(1, recipes.size());
        Assertions.assertEquals(expectedRecipeListDtos, recipes);
    }

    @Test
    void ReturnAListOfTwoRecipeListDtoFromGetAllFromPageOneWithStepThree() {
        List<RecipeListDto> expectedRecipeListDtos = List.of(
            new RecipeListDto(1, "Reis", "So muss Reis schmecken!", 0),
            new RecipeListDto(2, "Egg Fried Rice", "Ein schnelles asiatisches Gericht.", 0));
        List<RecipeListDto> recipes = recipeService.getRecipesFromPageInSteps(1, 3);
        Assertions.assertEquals(2, recipes.size());
        Assertions.assertEquals(expectedRecipeListDtos, recipes);
    }

    @Test
    void ReturnAnEmptyListOfRecipeListDtoFromGetAllFromPageTwoWithStepTwo() {
        List<RecipeListDto> recipes = recipeService.getRecipesFromPageInSteps(2, 2);
        Assertions.assertTrue(recipes.isEmpty());
    }

    @Test
    public void searchByNameShouldFound() {

        var recipe = recipeService.searchRecipe("Apfelkuchen");
        assertNotNull(recipe);
        assertThat(recipe)
            .hasSize(1);
    }

    @Test
    public void searchByNameShouldNoTFound() {

        var recipe = recipeService.searchRecipe("Gurke");
        assertEquals("[]",recipe.toString());
    }
}
