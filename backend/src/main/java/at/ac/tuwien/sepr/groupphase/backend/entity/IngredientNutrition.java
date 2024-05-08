package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity
public class IngredientNutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "nutrition_id", nullable = false)
    private Nutrition nutrition;

    @Column(name = "nutrition_value")  // Changed from "value" to "nutrition_value"
    private BigDecimal nutritionValue;  // Adjusted the field name accordingly

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public BigDecimal getValue() {  // Method name updated
        return nutritionValue;
    }

    public void setValue(BigDecimal nutritionValue) {  // Method name updated
        this.nutritionValue = nutritionValue;
    }

    public static final class IngredientNutritionBuilder {
        private Long id;
        private Ingredient ingredient;
        private Nutrition nutrition;
        private BigDecimal nutritionValue;

        private IngredientNutritionBuilder() {
        }

        public static IngredientNutritionBuilder anIngredientNutrition() {
            return new IngredientNutritionBuilder();
        }

        public IngredientNutritionBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public IngredientNutritionBuilder withIngredient(Ingredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public IngredientNutritionBuilder withNutrition(Nutrition nutrition) {
            this.nutrition = nutrition;
            return this;
        }

        public IngredientNutritionBuilder withValue(BigDecimal nutritionValue) {  // Method name updated
            this.nutritionValue = nutritionValue;
            return this;
        }

        public IngredientNutrition build() {
            IngredientNutrition ingredientNutrition = new IngredientNutrition();
            ingredientNutrition.setId(id);
            ingredientNutrition.setIngredient(ingredient);
            ingredientNutrition.setNutrition(nutrition);
            return ingredientNutrition;
        }
    }

}
