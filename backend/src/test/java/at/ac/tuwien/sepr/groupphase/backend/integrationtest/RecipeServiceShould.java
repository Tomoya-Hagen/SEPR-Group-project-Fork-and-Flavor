package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.RecipeServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        long recipeId=2;
        RecipeDetailDto recipe = recipeService.getRecipeDetailDtoById(recipeId);
        Assertions.assertNotNull(recipe);
//        Assertions.assertAll(
//            () -> Assertions.assertEquals(recipe.getName(), "Egg Fried Rice"),
//            () -> Assertions.assertEquals(
//                recipe.getCategories().size(), eggFriedRiceRecipe.getCategories().size()),
//            () -> Assertions.assertEquals(recipe.getOwner(), eggFriedRiceRecipe.getOwner()),
//            () -> Assertions.assertEquals(recipe.getDescription(), eggFriedRiceRecipe.getDescription()),
//            () -> Assertions.assertEquals(recipe.getNumberOfServings(), eggFriedRiceRecipe.getNumberOfServings()),
//            () -> Assertions.assertEquals(recipeIngredientRepository.getRecipeIngredientsByRecipeId(recipe.getId()).size(),
//                eggFriedRiceRecipe.getIngredients().size()),
//            () -> Assertions.assertEquals(recipe.getRecipeSteps().size(), eggFriedRiceRecipe.getRecipeSteps().size())
//        );
    }
}
