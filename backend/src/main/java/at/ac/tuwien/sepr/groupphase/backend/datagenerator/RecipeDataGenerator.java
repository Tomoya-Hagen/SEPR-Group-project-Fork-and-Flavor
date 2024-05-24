package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;


@Component
@Order(5)
public class RecipeDataGenerator extends DataGenerator implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final ResourceLoader resourceLoader;

    /**
     * The constructor for the RecipeBookDataGenerator class.
     *
     * @param resourceLoader The resource loader for loading resources.
     * @param recipeRepository The repository for Recipe objects.
     */
    public RecipeDataGenerator(RecipeRepository recipeRepository, ResourceLoader resourceLoader) {
        this.recipeRepository = recipeRepository;
        this.resourceLoader = resourceLoader;
    }

    /**
     * This method is run at application startup. It reads data from a CSV file and uses it to create and save RecipeBook objects.
     *
     * @param args The command line arguments.
     * @throws Exception If an error occurs while reading the file or saving the RecipeBook objects.
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

                recipeRepository.save(recipe);
            }
        }
    }
}

