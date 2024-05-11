package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.repository.AllergenRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Component
public class AllergenDataGenerator extends DataGenerator implements CommandLineRunner {

    private final AllergenRepository allergenRepository;

    private final ResourceLoader resourceLoader;

    public AllergenDataGenerator(AllergenRepository allergenRepository,
                                 ResourceLoader resourceLoader) {
        this.allergenRepository = allergenRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:allergens.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line);
                if (fields.size() >= 3) {
                    String type = fields.get(0).trim();
                    Optional<Allergen> existingAllergen = allergenRepository.findByType(type);
                    if (!existingAllergen.isPresent()) {
                        Allergen allergen = new Allergen();
                        allergen.setType(type);
                        allergen.setName(fields.get(1).trim());
                        allergen.setDescription(fields.get(2).trim().isEmpty() ? null : fields.get(2).trim());
                        allergenRepository.save(allergen);
                    }
                }
            }
        }
    }
}
