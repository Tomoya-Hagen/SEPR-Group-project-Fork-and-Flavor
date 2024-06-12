package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.IngredientNutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nutrition;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeDescriptionStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.entity.Role;
import at.ac.tuwien.sepr.groupphase.backend.repository.AllergenRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.NutritionRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeIngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeStepRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class DataGenerator implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final AllergenRepository allergenRepository;
    private final NutritionRepository nutritionRepository;
    private final RecipeBookRepository recipeBookRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeStepRepository recipeStepRepository;

    private final ResourceLoader resourceLoader;

    private HashMap<Long, Long> idMap;
    private List<Long> skippedRecipes = new ArrayList<>();

    public DataGenerator(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,
                         CategoryRepository categoryRepository, IngredientRepository ingredientRepository,
                         AllergenRepository allergenRepository, NutritionRepository nutritionRepository,
                         RecipeBookRepository recipeBookRepository, RecipeRepository recipeRepository,
                         RecipeIngredientRepository recipeIngredientRepository,
                         RecipeStepRepository recipeStepRepository, ResourceLoader resourceLoader) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.allergenRepository = allergenRepository;
        this.nutritionRepository = nutritionRepository;
        this.recipeBookRepository = recipeBookRepository;
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.resourceLoader = resourceLoader;
        this.idMap = new HashMap<>();
    }

    @Override
    public void run(String... args) throws Exception {
        generateUserData();
        generateCategoryData();
        generateAllergenData();
        generateNutritionData();
        generateIngredientData();
        generateRecipeData();
        generateRecipeIngredients();
        generateRecipeCategories();
        generateRecipeSteps();
        generateRecipeBooks();
    }

    private void generateUserData() {

        String[] roles = {"Admin", "User", "Contributor", "Cook", "StarCook"};
        String[] usernames = {"admin", "user", "contributor", "cook", "starcook"};
        String[] emails = {"admin@email.com", "user@email.com", "contributor@email.com", "cook@email.com", "starcook@email.com"};

        // Create and save roles
        List<Role> savedRoles = new ArrayList<>();
        for (String s : roles) {
            if (roleRepository.existsByName(s)) {
                continue;
            }
            Role role = new Role.RoleBuilder().withroleId(s).build();
            savedRoles.add(roleRepository.save(role));
        }

        // Create and save users and their roles
        for (int i = 0; i < usernames.length; i++) {
            // Check if a user with the same username already exists
            if (!userRepository.existsByUsername(usernames[i])) {
                List<Role> userRoles = new ArrayList<>();
                userRoles.add(savedRoles.get(i));
                ApplicationUser user = new ApplicationUser.ApplicationUserBuilder()
                    .withEmail(emails[i])
                    .withPassword(passwordEncoder.encode("password"))
                    .withUsername(usernames[i])
                    .withhasProfilePicture(false)
                    .withRoles(userRoles)
                    .build();
                userRepository.save(user);

            }
        }
        userRepository.flush();
    }

    protected void generateCategoryData() {
        Resource resource = resourceLoader.getResource("classpath:categories.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ',');
                if (fields.size() >= 2) {
                    String name = fields.get(0).trim();
                    String type = fields.get(1).trim();
                    Optional<Category> existingCategory = categoryRepository.findByNameAndType(name, type);
                    if (existingCategory.isEmpty()) {
                        Category category = Category.CategoryBuilder.aCategory()
                            .withName(name)
                            .withType(type)
                            .build();
                        categoryRepository.save(category);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        categoryRepository.flush();
    }

    protected void generateAllergenData() {
        Resource resource = resourceLoader.getResource("classpath:allergens.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ',');
                if (fields.size() >= 3) {
                    String type = fields.get(0).trim();
                    Optional<Allergen> existingAllergen = allergenRepository.findByType(type);
                    if (existingAllergen.isEmpty()) {
                        Allergen allergen = Allergen.AllergenBuilder.anAllergen()
                            .withType(type)
                            .withName(fields.get(1).trim())
                            .withDescription(fields.get(2).trim().isEmpty() ? null : fields.get(2).trim())
                            .build();
                        allergenRepository.save(allergen);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        allergenRepository.flush();
    }

    protected void generateNutritionData() {
        Resource resource = resourceLoader.getResource("classpath:nutrition.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ',');
                if (fields.size() == 3) {
                    String name = fields.get(0).trim();
                    Optional<Nutrition> existingNutrition = nutritionRepository.findByName(name);
                    if (existingNutrition.isEmpty()) {
                        Nutrition nutrition = Nutrition.NutritionBuilder.aNutrition()
                            .withName(fields.get(0).trim())
                            .withDescription(fields.get(1).trim())
                            .withUnit(fields.get(2).trim())
                            .build();
                        nutritionRepository.save(nutrition);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        nutritionRepository.flush();
    }

    protected void generateIngredientData() {
        String[] nutritionNames = {
            "Calories", "Fat Total", "Fat Saturated", "Protein", "Sodium",
            "Potassium", "Cholesterol", "Carbohydrates Total", "Fiber", "Sugar"
        };

        Map<String, Nutrition> nutritionMap = new HashMap<>();
        nutritionRepository.findAll().forEach(nutrition -> nutritionMap.put(nutrition.getName(), nutrition));
        Resource resource = resourceLoader.getResource("classpath:ingredients.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ',');
                if (fields.size() > 13) {
                    continue;
                }

                String name = fields.get(1).trim().replace("'", "");
                Optional<Ingredient> existingIngredient = ingredientRepository.findByName(name);
                if (!existingIngredient.isPresent()) {
                    long id = Long.parseLong(fields.get(0).trim());
                    Ingredient ingredient = Ingredient.IngredientBuilder.anIngredient()
                        .withName(name)
                        .withId(id)
                        .build();

                    // Handle allergens if any
                    if (!fields.get(2).trim().isEmpty()) {
                        Set<Allergen> allergens = findAllergensByCodes(fields.get(2).trim().replace("'", ""));
                        ingredient.setAllergens(allergens.stream().toList());
                    }

                    if (fields.size() == 13) {
                        // Add nutritional data
                        for (int i = 0; i < nutritionNames.length; i++) {
                            String value = fields.get(i + 3).trim();
                            if (!value.isEmpty()) {
                                Nutrition nutrition = nutritionMap.get(nutritionNames[i]);
                                if (nutrition != null) {
                                    addNutritionData(ingredient, nutrition, new BigDecimal(value));
                                }
                            }
                        }
                    }

                    ingredientRepository.save(ingredient);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ingredientRepository.flush();
    }

    protected void generateRecipeData() {
        List<String> names = recipeRepository.getAllNames();
        Resource resource = resourceLoader.getResource("classpath:recipe.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ';');
                if (first) {
                    first = false;
                    continue;
                }
                Long id = Long.parseLong(fields.get(0));
                if (names.contains(fields.get(1))) {
                    skippedRecipes.add(id);
                    continue;
                }
                Recipe forkedFrom = null;
                if (!fields.get(4).isEmpty()) {
                    forkedFrom = recipeRepository.findFirstById(idMap.get(Long.parseLong(fields.get(4))));
                }
                ApplicationUser user = userRepository.findFirstById(Long.parseLong(fields.get(5)));
                Recipe recipe = Recipe.RecipeBuilder.aRecipe()
                    .withName(fields.get(1))
                    .withDescription(fields.get(2))
                    .withNumberOfServings(Short.parseShort(fields.get(3)))
                    .withOwner(user)
                    .withForkedFrom(forkedFrom)
                    .build();
                recipeRepository.save(recipe);
                idMap.put(id, recipe.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        recipeRepository.flush();
    }

    protected void generateRecipeIngredients() {
        Resource resource = resourceLoader.getResource("classpath:recipeIngredients.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ';');
                if (first) {
                    first = false;
                    continue;
                }
                if (skippedRecipes.contains(Long.parseLong(fields.get(0)))) {
                    continue;
                }
                RecipeIngredient.Unit unit = RecipeIngredient.getUnitFromString(fields.get(3));
                if (unit == null) {
                    unit = RecipeIngredient.Unit.EMPTY;
                }
                BigDecimal amount = BigDecimal.valueOf(1);
                Boolean amountGiven = true;
                try {
                    amount = new BigDecimal(fields.get(2).trim());
                } catch (NumberFormatException e) {
                    amountGiven = false;
                }
                Recipe recipe = recipeRepository.findById(idMap.get(Long.parseLong(fields.get(0)))).orElse(null);
                Ingredient ingredient = ingredientRepository.findByName(fields.get(1)).get();
                RecipeIngredient recipeIngredient;
                if (amountGiven) {
                    recipeIngredient = RecipeIngredient.RecipeIngredientBuilder.aRecipeIngredient()
                        .withRecipe(recipe)
                        .withIngredient(ingredient)
                        .withAmount(amount)
                        .withUnit(unit)
                        .build();
                } else {
                    recipeIngredient = RecipeIngredient.RecipeIngredientBuilder.aRecipeIngredient()
                        .withRecipe(recipe)
                        .withIngredient(ingredient)
                        .withUnit(unit)
                        .build();
                }
                recipeIngredientRepository.save(recipeIngredient);
            }
            recipeIngredientRepository.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void generateRecipeCategories() {
        Resource resource = resourceLoader.getResource("classpath:recipeCategories.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ';');
                if (first) {
                    first = false;
                    continue;
                }
                if (skippedRecipes.contains(Long.parseLong(fields.get(0)))) {
                    continue;
                }
                Recipe recipe = recipeRepository.findById(idMap.get(Long.parseLong(fields.get(0)))).orElse(null);
                List<Category> category = categoryRepository.findByName(fields.get(1));
                recipe.setCategories(category);
                recipeRepository.save(recipe);
            }
            recipeRepository.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void generateRecipeSteps() {
        Resource resource = resourceLoader.getResource("classpath:recipeSteps.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ';');
                if (first) {
                    first = false;
                    continue;
                }
                if (skippedRecipes.contains(Long.parseLong(fields.get(0)))) {
                    continue;
                }
                Recipe recipe = recipeRepository.findById(idMap.get(Long.parseLong(fields.get(0)))).orElse(null);
                RecipeStep recipeStep;
                if (fields.size() == 4) {
                    // Beschreibungsschritt
                    recipeStep = RecipeDescriptionStep.RecipeDescriptionStepBuilder.aRecipeDescriptionStep()
                        .withRecipe(recipe)
                        .withStepNumber(Integer.parseInt(fields.get(1)))
                        .withName(fields.get(2))
                        .withDescription(fields.get(3))
                        .build();
                } else {
                    // Unterrezept
                    Long unterrezeptId = idMap.get(Long.parseLong(fields.get(4)));
                    Recipe unterrezept = recipeRepository.findFirstById(unterrezeptId);
                    recipeStep = RecipeRecipeStep.RecipeRecipeStepBuilder.aRecipeRecipeStep()
                        .withRecipe(recipe)
                        .withStepNumber(Integer.parseInt(fields.get(1)))
                        .withName(fields.get(2))
                        .withRecipeRecipe(unterrezept)
                        .build();
                }
                recipeStepRepository.save(recipeStep);
            }
            recipeStepRepository.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void generateRecipeBooks() {
        Resource resource = resourceLoader.getResource("classpath:recipebook.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            Boolean first = true;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line, ';');
                if (first) {
                    first = false;
                    continue;
                }
                if (fields.size() == 5) {
                    String name = fields.get(1).trim();
                    if (recipeBookRepository.existsByName(name)) {
                        continue;
                    }
                    String description = fields.get(2).trim();
                    Long userid = Long.parseLong(fields.get(3));
                    List<String> recipeIds;
                    recipeIds = parseCsvLine(fields.get(4), ',');
                    List<Recipe> recipes = new java.util.ArrayList<>(List.of());
                    if (!recipeIds.isEmpty()) {
                        for (String recipeId : recipeIds) {
                            if (recipeId.isEmpty()) {
                                continue;
                            }
                            recipeRepository.findById(idMap.get(Long.parseLong(recipeId))).ifPresent(recipes::add);
                        }
                    }
                    ApplicationUser user = userRepository.findFirstById(userid);
                    RecipeBook recipeBook = RecipeBook.RecipeBookBuilder.aRecipeBook()
                        .withName(name)
                        .withDescription(description)
                        .withOwner(user)
                        .withRecipes(recipes)
                        .build();
                    recipeBookRepository.save(recipeBook);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recipeBookRepository.flush();
    }

    private List<String> parseCsvLine(String line, char separator) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        char[] chars = line.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (ch == '"' && (i == 0 || chars[i - 1] != '\\')) {
                inQuotes = !inQuotes;
            } else if (ch == separator && !inQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }
        fields.add(field.toString());
        return fields;
    }

    private Set<Allergen> findAllergensByCodes(String codes) {
        Set<Allergen> allergens = new HashSet<>();
        for (char code : codes.toCharArray()) {
            Optional<Allergen> optionalAllergen = allergenRepository.findByType(String.valueOf(code));
            optionalAllergen.ifPresent(allergens::add);
        }
        return allergens;
    }

    private void addNutritionData(Ingredient ingredient, Nutrition nutrition, BigDecimal value) {
        IngredientNutrition newNutrition = new IngredientNutrition();
        newNutrition.setNutrition(nutrition);
        newNutrition.setIngredient(ingredient);
        newNutrition.setValue(value);
        List<IngredientNutrition> nutritions = ingredient.getNutritions();
        nutritions.add(newNutrition);
        ingredient.setNutritions(nutritions);
    }

}
