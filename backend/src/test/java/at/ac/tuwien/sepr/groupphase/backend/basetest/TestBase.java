package at.ac.tuwien.sepr.groupphase.backend.basetest;

import at.ac.tuwien.sepr.groupphase.backend.datagenerator.AllergenDataGenerator;
import at.ac.tuwien.sepr.groupphase.backend.datagenerator.CategoryDataGenerator;
import at.ac.tuwien.sepr.groupphase.backend.datagenerator.IngredientDataGenerator;
import at.ac.tuwien.sepr.groupphase.backend.datagenerator.NutritionDataGenerator;
import at.ac.tuwien.sepr.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeIngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is used to prepare the data storage before and after each test
 */
public abstract class TestBase {
    @Autowired
    private AllergenDataGenerator allergenDataGenerator;
    @Autowired
    private CategoryDataGenerator categoryDataGenerator;
    @Autowired
    private IngredientDataGenerator ingredientDataGenerator;
    @Autowired
    private NutritionDataGenerator nutritionDataGenerator;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @BeforeEach
    public void setupDb() throws Exception {
        allergenDataGenerator.run();
        categoryDataGenerator.run();
        ingredientDataGenerator.run();
        nutritionDataGenerator.run();
    }

    @AfterEach
    public void tearDownDb() {
        recipeIngredientRepository.deleteAll();
        recipeRepository.deleteAll();
    }
}
