package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
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
public class RecipeBookDataGenerator extends DataGenerator implements CommandLineRunner {

    private final RecipeBookRepository recipeBookRepository;
    private final ResourceLoader resourceLoader;

    public RecipeBookDataGenerator(RecipeBookRepository recipeBookRepository, ResourceLoader resourceLoader) {
        this.recipeBookRepository = recipeBookRepository;
        this.resourceLoader = resourceLoader;
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
                recipeBookRepository.save(recipeBook);
            }
        }
    }
}
