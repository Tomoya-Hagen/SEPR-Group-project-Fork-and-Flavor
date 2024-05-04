package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.*;

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
}
