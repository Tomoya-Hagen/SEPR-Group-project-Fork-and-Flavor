package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "ingredient_allergen",
        joinColumns = @JoinColumn(name = "ingredient_id"),
        inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Allergen> allergens = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private List<IngredientNutrition> nutritions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")

    private List<RecipeIngredient> recipesIngredients = new ArrayList<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Allergen> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<Allergen> allergens) {
        this.allergens = allergens;
    }

    public List<IngredientNutrition> getNutritions() {
        return nutritions;
    }

    public void setNutritions(List<IngredientNutrition> nutritions) {
        this.nutritions = nutritions;
    }

    public List<RecipeIngredient> getRecipesIngredients() {
        return recipesIngredients;

    public void addNutritionData(Nutrition nutrition, BigDecimal value) {
        IngredientNutrition ingredientNutrition = IngredientNutrition.IngredientNutritionBuilder.anIngredientNutrition()
            .withIngredient(this)
            .withNutrition(nutrition)
            .withValue(value)
            .build();
        this.nutritionData.add(ingredientNutrition);
    }

    public static final class IngredientBuilder {
        private Long id;
        private String name;
        private Set<Allergen> allergens;

        private IngredientBuilder() {
        }

        public static IngredientBuilder anIngredient() {
            return new IngredientBuilder();
        }

        public IngredientBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public IngredientBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public IngredientBuilder withAllergens(Set<Allergen> allergens) {
            this.allergens = allergens;
            return this;
        }

        public Ingredient build() {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(id);
            ingredient.setName(name);
            ingredient.setAllergens(allergens);
            return ingredient;
        }
    }
}
