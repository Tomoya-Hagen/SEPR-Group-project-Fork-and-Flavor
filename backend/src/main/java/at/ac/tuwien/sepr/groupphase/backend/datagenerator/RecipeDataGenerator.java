package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Profile("generateData")
@Component
@Order(2)
public class RecipeDataGenerator {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    public RecipeDataGenerator(RecipeRepository recipeRepository,
                               CategoryRepository categoryRepository,
                               IngredientRepository ingredientRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @PostConstruct
    private void generateRecipes() {
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
            List.of(new RecipeIngredient(riceRecipe, ingredientRepository.findByName("Basmatireis").get(), BigDecimal.ONE, 1),
                new RecipeIngredient(riceRecipe, ingredientRepository.findByName("Salz").get(), BigDecimal.ONE, 1)
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
                ingredientRepository.findByName("Zwiebel").get(), BigDecimal.ONE, 1),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Ei").get(), BigDecimal.ONE, 1),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Knoblauch").get(), BigDecimal.ONE, 1),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Jungzwiebel").get(), BigDecimal.ONE, 1),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Sesamöl").get(), BigDecimal.ONE, 1)
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
    }
}
