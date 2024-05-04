package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.repository.AllergenRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Order(2)
public class IngredientDataGenerator implements CommandLineRunner {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private AllergenRepository allergenRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:ingredients.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                String name = parts[0].trim().replace("\'", "");
                Optional<Ingredient> existingIngredient = ingredientRepository.findByName(name);
                if (!existingIngredient.isPresent()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(name);

                    if (parts.length > 1) {
                        String allergenCodes = parts[1].trim().replace("\'", "");
                        Set<Allergen> allergens = findAllergensByCodes(allergenCodes);
                        ingredient.setAllergens(allergens);
                    }

                    ingredientRepository.save(ingredient);
                }
            }
        }
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
