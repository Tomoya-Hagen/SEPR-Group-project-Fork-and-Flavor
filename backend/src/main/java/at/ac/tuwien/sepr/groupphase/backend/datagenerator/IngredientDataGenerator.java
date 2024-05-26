package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.repository.AllergenRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.NutritionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Order(3)
public class IngredientDataGenerator implements CommandLineRunner {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private AllergenRepository allergenRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String[] NUTRITION_NAMES = {
        "Calories", "Fat Total", "Fat Saturated", "Protein", "Sodium",
        "Potassium", "Cholesterol", "Carbohydrates Total", "Fiber", "Sugar"
    };

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Map<String, Nutrition> nutritionMap = loadNutritionMap();
        Resource resource = resourceLoader.getResource("classpath:ingredients.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length > 12) {
                    continue;
                }

                String name = parts[0].trim().replace("'", "");
                Optional<Ingredient> existingIngredient = ingredientRepository.findByName(name);
                if (!existingIngredient.isPresent()) {
                    Ingredient ingredient = Ingredient.IngredientBuilder.anIngredient()
                        .withName(name)
                        .build();

                    // Handle allergens if any
                    if (!parts[1].trim().isEmpty()) {
                        Set<Allergen> allergens = findAllergensByCodes(parts[1].trim().replace("'", ""));
                        ingredient.setAllergens(allergens);
                    }

                    if (parts.length == 12) {
                        // Add nutritional data
                        for (int i = 0; i < NUTRITION_NAMES.length; i++) {
                            String value = parts[i + 2].trim();
                            if (!value.isEmpty()) {
                                Nutrition nutrition = nutritionMap.get(NUTRITION_NAMES[i]);
                                if (nutrition != null) {
                                    ingredient.addNutritionData(nutrition, new BigDecimal(value));
                                }
                            }
                        }
                    }

                    ingredientRepository.save(ingredient);
                }
            }
        }
    }

    private Map<String, Nutrition> loadNutritionMap() {
        Map<String, Nutrition> map = new HashMap<>();
        nutritionRepository.findAll().forEach(nutrition -> map.put(nutrition.getName(), nutrition));
        return map;
    }

    private Set<Allergen> findAllergensByCodes(String codes) {
        Set<Allergen> allergens = new HashSet<>();
        for (char code : codes.toCharArray()) {
            Optional<Allergen> optionalAllergen = allergenRepository.findByType(String.valueOf(code));
            optionalAllergen.ifPresent(allergens::add);
        }
        return allergens;
    }
}
