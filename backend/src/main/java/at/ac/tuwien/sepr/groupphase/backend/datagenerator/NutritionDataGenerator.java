package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.repository.NutritionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@Order(2)
public class NutritionDataGenerator implements CommandLineRunner {

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private ResourceLoader resourceLoader;

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
                Nutrition nutrition = Nutrition.NutritionBuilder.aNutrition()
                    .withName(parts[0].trim().replace("\"", ""))
                    .withDescription(parts[1].trim().replace("\"", ""))
                    .withUnit(parts[2].trim().replace("\"", ""))
                    .build();
                nutritionRepository.save(nutrition);
            }
        }
    }
}
