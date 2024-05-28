package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

@Component
@Order(5)
public class RecipeDataGenerator extends DataGenerator implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    //  private final IngredientRepository ingredientRepository;
    //  private final CategoryRepository categoryRepository;
    private final ResourceLoader resourceLoader;

    /**
     * The constructor for the RecipeDataGenerator class.
     *
     * @param resourceLoader The resource loader for loading resources.
     * @param recipeRepository The repository for Recipe objects.
     */
    public RecipeDataGenerator(RecipeRepository recipeRepository, ResourceLoader resourceLoader) {
        this.recipeRepository = recipeRepository;
        this.resourceLoader = resourceLoader;
    }

    /**
     * This method is run at application startup. It reads data from a CSV file and uses it to create and save Recipe objects.
     *
     * @param args The command line arguments.
     * @throws Exception If an error occurs while reading the file or saving the Recipe objects.
     */
    @Transactional
    @Override
    public void run(String... args) throws Exception {
        String line;
        ApplicationUser user = new ApplicationUser();
        user.setId(1);
        Resource resource = resourceLoader.getResource("classpath:recipe.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] record = line.split(";");
                Recipe recipe = new Recipe();
                recipe.setId(Long.parseLong(record[0]));
                recipe.setName(record[1]);
                recipe.setDescription("");
                recipe.setCategories(null);

                recipe.setOwner(user);

                recipeRepository.save(recipe);
            }
        }
    }


    /*


    @Transactional
    @Override
    public void run(String... args) throws Exception {
        ApplicationUser user = new ApplicationUser();
        user.setId(1);
        Recipe riceRecipe = new Recipe();
        riceRecipe.setId(1L);
        riceRecipe.setName("Reis");
        riceRecipe.setDescription("So muss Reis schmecken!");
        riceRecipe.setNumberOfServings((short) 1);
        riceRecipe.setForkedFrom(null);
        riceRecipe.setOwner(user);
        riceRecipe.setCategories(List.of(
            categoryRepository.findByNameAndType("Beilage", "SIDE_DISH").orElseThrow()
        ));
        riceRecipe.setIngredients(
            List.of(new RecipeIngredient(riceRecipe, ingredientRepository.findByName("Basmatireis").orElseThrow(), BigDecimal.valueOf(1), RecipeIngredient.Unit.mg),
                new RecipeIngredient(riceRecipe, ingredientRepository.findByName("Salz").orElseThrow(), BigDecimal.valueOf(2), RecipeIngredient.Unit.L)
            ));
        riceRecipe.setRecipeSteps(List.of(
            new RecipeDescriptionStep("Wasser kochen", "Einen Topf mit Wasser befüllen. Reichlich salzen und das Wasser zum Kochen bringen.", riceRecipe, 1),
            new RecipeDescriptionStep("Reis kochen", "Wenn das Wasser kocht, den Reis hinzugeben und 5-8 Minuten kochen. Zwischendurch umrühren. ", riceRecipe, 2),
            new RecipeDescriptionStep("Reis dämpfen",
                "Danach den Reis in ein Sieb abgießen, "
                    + "den Topf 3 cm hoch mit Wasser befüllen und das Wasser zum Kochen bringen. "
                    + "Das Sieb mit dem Reis auf den Topf hängen und mit Alufolie abdecken. "
                    + "Die Temperatur kann nun auf ca. 1/3 der maximalen Temperatur reduziert werden. "
                    + "Nach 10 minütigem Dämpfen ist der Reis locker und lecker und kann serviert werden. ",
                riceRecipe, 3)
        ));
        if (recipeRepository.getRecipeById(1L).isEmpty()) {
            recipeRepository.save(riceRecipe);
        }

        Recipe eggFriedRiceRecipe = new Recipe();
        eggFriedRiceRecipe.setId(2L);
        eggFriedRiceRecipe.setOwner(user);
        eggFriedRiceRecipe.setName("Egg Fried Rice");
        eggFriedRiceRecipe.setDescription("Ein schnelles asiatisches Gericht.");
        eggFriedRiceRecipe.setCategories(List.of(
            categoryRepository.findByNameAndType("Hautpspeise", "MAIN_COURSE").orElseThrow())
        );
        eggFriedRiceRecipe.setNumberOfServings((short) 1);
        eggFriedRiceRecipe.setIngredients(List.of(
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Zwiebel").orElseThrow(), BigDecimal.valueOf(3), RecipeIngredient.Unit.mg),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Ei").orElseThrow(), BigDecimal.valueOf(4), RecipeIngredient.Unit.g),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Knoblauch").orElseThrow(), BigDecimal.valueOf(5), RecipeIngredient.Unit.g),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Jungzwiebel").orElseThrow(), BigDecimal.valueOf(6), RecipeIngredient.Unit.mg),
            new RecipeIngredient(eggFriedRiceRecipe,
                ingredientRepository.findByName("Sesamöl").orElseThrow(), BigDecimal.valueOf(7), RecipeIngredient.Unit.g)
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
                "Ei hinzufügen und verrühren. Reis, Sesamöl, Sojasauce, "
                    + "Glutamat und Pfeffer hinzufügen und weiter unter ständigem Rühren braten.",
                eggFriedRiceRecipe, 4),
            new RecipeDescriptionStep("Servieren",
                " Wenn der gewünschte Bräunungsgrad erreicht ist, "
                    + "Frühlingszwiebeln hinzufügen und servieren.",
                eggFriedRiceRecipe, 5)

        ));
        if (recipeRepository.getRecipeById(2L).isEmpty()) {
            recipeRepository.save(eggFriedRiceRecipe);
        }
    }

     */
}
