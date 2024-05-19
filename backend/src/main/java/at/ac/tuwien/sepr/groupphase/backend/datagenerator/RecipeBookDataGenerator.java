package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
@Order(5)
public class RecipeBookDataGenerator extends DataGenerator implements CommandLineRunner {


    @Override
    @Transactional
    public void run(String... args) throws Exception {

    }
}
