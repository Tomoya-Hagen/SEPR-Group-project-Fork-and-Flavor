package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.repository.NutritionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Profile("generateData")
@Component
@Order(2)
public class NutritionDataGenerator implements CommandLineRunner {

    private final NutritionRepository nutritionRepository;

    private final ResourceLoader resourceLoader;

    public NutritionDataGenerator(NutritionRepository nutritionRepository,
                                 ResourceLoader resourceLoader) {
        this.nutritionRepository = nutritionRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(String... args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:nutrition.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            reader.readLine(); // Skip header if your CSV has one
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 3) {
                    continue;
                }

                Nutrition nutrition = new Nutrition();
                nutrition.setName(parts[0].trim().replace("\"", ""));
                nutrition.setDescription(parts[1].trim().replace("\"", ""));
                nutrition.setUnit(parts[2].trim().replace("\"", ""));
                nutritionRepository.save(nutrition);
            }
        }
    }
}
