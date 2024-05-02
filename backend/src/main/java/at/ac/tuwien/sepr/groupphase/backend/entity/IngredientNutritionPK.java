package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class IngredientNutritionPK implements Serializable {
    @Column(name = "ingredient_id")
    @Id
    private long ingredientId;
    @Column(name = "nutrition_id")
    @Id
    private long nutritionId;

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public long getNutritionId() {
        return nutritionId;
    }

    public void setNutritionId(long nutritionId) {
        this.nutritionId = nutritionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientNutritionPK that = (IngredientNutritionPK) o;
        return Objects.equals(ingredientId, that.ingredientId) && Objects.equals(nutritionId, that.nutritionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, nutritionId);
    }
}
