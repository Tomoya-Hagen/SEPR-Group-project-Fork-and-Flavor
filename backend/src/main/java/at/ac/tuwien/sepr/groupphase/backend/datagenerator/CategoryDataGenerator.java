package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Profile("generateData")
@Component
@Order(3)
public class CategoryDataGenerator extends DataGenerator implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    private final ResourceLoader resourceLoader;

    public CategoryDataGenerator(CategoryRepository categoryRepository,
                                 ResourceLoader resourceLoader) {
        this.categoryRepository = categoryRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:categories.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line);
                if (fields.size() >= 2) {
                    String name = fields.get(0).trim();
                    String type = fields.get(1).trim();
                    Optional<Category> existingCategory = categoryRepository.findByNameAndType(name, type);
                    if (!existingCategory.isPresent()) {
                        Category category = new Category();
                        category.setName(name);
                        category.setType(type);
                        categoryRepository.save(category);
                    }
                }
            }
        }
    }
}
