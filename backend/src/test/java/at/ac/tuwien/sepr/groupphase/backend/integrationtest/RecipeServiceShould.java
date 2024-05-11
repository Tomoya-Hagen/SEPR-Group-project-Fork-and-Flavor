package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
@ActiveProfiles({"test", "generateData"})
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RecipeServiceShould extends TestBase {
    @Autowired
    private RecipeService recipeService;

    @Test
    void ReturnARecipeDetailDtoIfARecipeWithTheGivenRecipeIdExists() {
        long recipeId = 2;
        ArrayList<IngredientDetailDto> ingredientDetailDtos = new java.util.ArrayList<>(List.of(
            new IngredientDetailDto(5, "Basmatireis", new BigDecimal("1.00"), 1),
            new IngredientDetailDto(133, "Salz", new BigDecimal("2.00"), 1),
            new IngredientDetailDto(189, "Zwiebel", new BigDecimal("3.00"), 1),
            new IngredientDetailDto(23, "Ei", new BigDecimal("4.00"), 1),
            new IngredientDetailDto(68, "Knoblauch", new BigDecimal("5.00"), 1),
            new IngredientDetailDto(58, "Jungzwiebel", new BigDecimal("6.00"), 1),
            new IngredientDetailDto(147, "Sesamöl", new BigDecimal("7.00"), 1)));
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
                new CategoryDetailDto(2, "Hautpspeise", "MAIN_COURSE")}, recipe.categories().toArray()),
            () -> Assertions.assertEquals(1, recipe.ownerId()),
            () -> Assertions.assertEquals("Ein schnelles asiatisches Gericht.", recipe.description()),
            () -> Assertions.assertEquals(1, recipe.numberOfServings().intValue()),
            () -> Assertions.assertArrayEquals(ingredientDetailDtos.toArray()
                , actualIngredients.toArray()),
            () -> Assertions.assertEquals(5, recipe.recipeSteps().size()),
            () -> Assertions.assertEquals(2, recipe.allergens().size()),
            () -> Assertions.assertEquals(new BigDecimal("1366.40"),recipe.nutritions().stream().filter(n->n.id() == 1).findFirst().get().value()));
    }

    @Test
    void ReturnANotFoundExceptionIfNoRecipeWasFoundWithTheGivenId() {
        long recipeId = 2000;
        Assertions.assertThrows(NotFoundException.class, () -> recipeService.getRecipeDetailDtoById(recipeId));
    }
}
