package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeIngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Test
    void ReturnARecipeIfARecipeWithTheGivenRecipeIdExists() {
        ApplicationUser user = new ApplicationUser();
        user.setId(1);
        Recipe riceRecipe = new Recipe();
        riceRecipe.setName("Reis");
        riceRecipe.setDescription("So muss Reis schmecken!");
        riceRecipe.setNumberOfServings((short) 1);
        riceRecipe.setForkedFrom(null);
        riceRecipe.setOwner(user);
        riceRecipe.setCategories(List.of(
            categoryRepository.findByNameAndType("Beilage", "SIDE_DISH").get()
        ));
        riceRecipe.setIngredients(
            List.of(new RecipeIngredient(riceRecipe, ingredientRepository.findByName("Basmatireis").get(), BigDecimal.ONE, RecipeIngredient.Unit.mg),
                new RecipeIngredient(riceRecipe, ingredientRepository.findByName("Salz").get(), BigDecimal.ONE, RecipeIngredient.Unit.L)
            ));
        riceRecipe.setRecipeSteps(List.of(
            new RecipeDescriptionStep("Wasser kochen", "Einen Topf mit Wasser befüllen. Reichlich salzen und das Wasser zum Kochen bringen.", riceRecipe, 1),
            new RecipeDescriptionStep("Reis kochen", "Wenn das Wasser kocht, den Reis hinzugeben und 5-8 Minuten kochen. Zwischendurch umrühren. ", riceRecipe, 2),
            new RecipeDescriptionStep("Reis dämpfen",
                "Danach den Reis in ein Sieb abgießen, " +
                    "den Topf 3 cm hoch mit Wasser befüllen und das Wasser zum Kochen bringen. " +
                    "Das Sieb mit dem Reis auf den Topf hängen und mit Alufolie abdecken. " +
                    "Die Temperatur kann nun auf ca. 1/3 der maximalen Temperatur reduziert werden. " +
                    "Nach 10 minütigem Dämpfen ist der Reis locker und lecker und kann serviert werden. ",
                riceRecipe, 3)
        ));
        recipeRepository.save(riceRecipe);
        Recipe eggFriedRiceRecipe = new Recipe();
        eggFriedRiceRecipe.setOwner(user);
        eggFriedRiceRecipe.setName("Egg Fried Rice");
        eggFriedRiceRecipe.setDescription("Ein schnelles asiatisches Gericht.");
        eggFriedRiceRecipe.setCategories(List.of(
            categoryRepository.findByNameAndType("Hautpspeise", "MAIN_COURSE").get())
        );
        eggFriedRiceRecipe.setNumberOfServings((short) 1);
        eggFriedRiceRecipe.setIngredients(List.of(
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Zwiebel").get(), BigDecimal.ONE, RecipeIngredient.Unit.mg),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Ei").get(), BigDecimal.ONE, RecipeIngredient.Unit.g),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Knoblauch").get(), BigDecimal.ONE, RecipeIngredient.Unit.g),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Jungzwiebel").get(), BigDecimal.ONE, RecipeIngredient.Unit.mg),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Sesamöl").get(), BigDecimal.ONE, RecipeIngredient.Unit.g)
        ));
        eggFriedRiceRecipe.setRecipeSteps(List.of(
            new RecipeRecipeStep("Reis kochen eigentlich sollte er ein Tag alt sein aber das ist ein Beispiel",
                eggFriedRiceRecipe, 1, riceRecipe),
            new RecipeDescriptionStep("Gemüse hacken",
                "Frühlingszwiebel, Zwiebel, Knoblauch klein hacken.",
                eggFriedRiceRecipe, 2),
            new RecipeDescriptionStep("Gemüse anbraten",
                "Pflanzenöl im Wok erhitzen, Schalotten, Knoblauch und Chili hinzufügen und scharf anbraten.",
                eggFriedRiceRecipe, 3),
            new RecipeDescriptionStep("Restliche Zutaten anbraten",
                "Ei hinzufügen und verrühren. Reis, Sesamöl, Sojasauce, " +
                    "Glutamat und Pfeffer hinzufügen und weiter unter ständigem Rühren braten.",
                eggFriedRiceRecipe, 4),
            new RecipeDescriptionStep("Servieren",
                " Wenn der gewünschte Bräunungsgrad erreicht ist, " +
                    "Frühlingszwiebeln hinzufügen und servieren.",
                eggFriedRiceRecipe, 5)

        ));
        recipeRepository.save(eggFriedRiceRecipe);
        Optional<Recipe> recipe = recipeRepository.getRecipeById(eggFriedRiceRecipe.getId());
        Assertions.assertNotNull(recipe);
        Recipe newRecipe = recipe.get();
        Assertions.assertAll(
            () -> Assertions.assertEquals(newRecipe.getName(), eggFriedRiceRecipe.getName()),
            () -> Assertions.assertEquals(
                newRecipe.getCategories().size(), eggFriedRiceRecipe.getCategories().size()),
            () -> Assertions.assertEquals(newRecipe.getOwner(), eggFriedRiceRecipe.getOwner()),
            () -> Assertions.assertEquals(newRecipe.getDescription(), eggFriedRiceRecipe.getDescription()),
            () -> Assertions.assertEquals(newRecipe.getNumberOfServings(), eggFriedRiceRecipe.getNumberOfServings()),
            () -> Assertions.assertEquals(recipeIngredientRepository.getRecipeIngredientsByRecipeId(newRecipe.getId()).size(),
                eggFriedRiceRecipe.getIngredients().size()),
            () -> Assertions.assertEquals(newRecipe.getRecipeSteps().size(), eggFriedRiceRecipe.getRecipeSteps().size())
        );
    }

    @Test
    void ReturnAnEmptyOptionalIfNoRecipeWithAGivenRecipeIdExists() {
        Optional<Recipe> recipe = recipeRepository.getRecipeById(-1000L);
        Assertions.assertFalse(recipe.isPresent());
    }

    @Test
    void ReturnOneRecipeFromGetAllFromIdOneToOne() {
        List<Recipe> recipes = recipeRepository.getAllRecipesWithIdFromTo(1, 1);
        Assertions.assertEquals(1, recipes.size());
        Assertions.assertEquals(recipeRepository.getRecipeById(1).orElseThrow(), recipes.getFirst());
    }

    @Test
    void ReturnTwoRecipesFromGetAllFromIdOneToTwo() {
        List<Recipe> expectedRecipes = List.of(
            recipeRepository.getRecipeById(1).orElseThrow(),
            recipeRepository.getRecipeById(2).orElseThrow());
        List<Recipe> recipes = recipeRepository.getAllRecipesWithIdFromTo(1, 2);
        Assertions.assertEquals(2, recipes.size());
        Assertions.assertEquals(expectedRecipes, recipes);
    }

    @Test
    void ReturnNoRecipesFromGetAllFromIdThreeToFour() {
        List<Recipe> recipes = recipeRepository.getAllRecipesWithIdFromTo(3000, 3001);
        Assertions.assertTrue(recipes.isEmpty());
    }

    @Test
    void searchReturnsEmptyListWhenNoNameMatches() {
        assertEquals(Collections.emptyList(), recipeRepository.search("Kaschew"));
    }

    @Test
    void searchReturnsRecipeRegardlessOfCase() {
        assertEquals("Zitronenkuchen", recipeRepository.search("zitronenKuchen").getFirst().getName());
    }

    @Test
    void searchReturnsRecipeWhenNameMatches() {
        assertEquals("Gratin", recipeRepository.search("Gratin").getFirst().getName());
    }

}
