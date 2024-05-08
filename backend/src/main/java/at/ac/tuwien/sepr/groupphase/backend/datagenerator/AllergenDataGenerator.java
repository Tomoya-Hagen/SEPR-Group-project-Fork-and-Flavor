package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.repository.AllergenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Order(1)
public class AllergenDataGenerator implements CommandLineRunner {

    @Autowired
    private AllergenRepository allergenRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:allergens.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parsecsvline(line);
                if (fields.size() >= 3) {
                    String type = fields.get(0).trim();
                    Optional<Allergen> existingAllergen = allergenRepository.findByType(type);
                    if (!existingAllergen.isPresent()) {
                        Allergen allergen = Allergen.AllergenBuilder.anAllergen()
                            .withType(type)
                            .withName(fields.get(1).trim())
                            .withDescription(fields.get(2).trim().isEmpty() ? null : fields.get(2).trim())
                            .build();
                        allergenRepository.save(allergen);
                    }
                }
            }
        }
    }


    private List<String> parsecsvline(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        char[] chars = line.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (ch == '"' && (i == 0 || chars[i - 1] != '\\')) {  // Handle quote at start or if not escaped
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {  // If comma is delimiter (outside quotes)
                fields.add(field.toString());
                field.setLength(0);  // Reset StringBuilder for next field
            } else {
                field.append(ch);  // Add the character to the current field
            }
        }

        // Add the last field (post final comma)
        fields.add(field.toString());
        return fields;
    }
}
