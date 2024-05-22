package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Component
@Order(5)
public class RecipeBookDataGenerator extends DataGenerator implements CommandLineRunner {

    private final RecipeBookRepository recipeBookRepository;
    private final ResourceLoader resourceLoader;
    private final RecipeRepository recipeRepository;

    public RecipeBookDataGenerator(RecipeBookRepository recipeBookRepository, ResourceLoader resourceLoader, RecipeRepository recipeRepository) {
        this.recipeBookRepository = recipeBookRepository;
        this.resourceLoader = resourceLoader;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        String line;
        Resource resource = resourceLoader.getResource("classpath:recipebook.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] record = line.split(";");
                RecipeBook recipeBook = new RecipeBook();
                recipeBook.setId(Long.parseLong(record[0]));
                recipeBook.setName(record[1]);
                recipeBook.setDescription(record[2]);
                recipeBook.setOwnerId(Long.parseLong(record[3]));

                List<Recipe> recipe = new java.util.ArrayList<>(List.of());
                for (int i = 3; i < record.length; i++) {
                    recipe.add(recipeRepository.findById(Long.parseLong(record[i])).orElseThrow(NotFoundException::new));
                }
                recipeBook.setRecipes(recipe);

                recipeBookRepository.save(recipeBook);
            }
        }
    }
}
