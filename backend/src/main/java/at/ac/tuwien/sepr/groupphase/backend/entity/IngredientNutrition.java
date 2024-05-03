package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "Ingredient_Nutrition", schema = "PUBLIC", catalog = "DB")
@IdClass(Ingredientnutritionpk.class)
public class IngredientNutrition {
    @Id
    @Column(name = "ingredient_id")
    private long ingredientId;

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    @Id
    @Column(name = "nutrition_id")
    private long nutritionId;

    public long getNutritionId() {
        return nutritionId;
    }

    public void setNutritionId(long nutritionId) {
        this.nutritionId = nutritionId;
    }

    @Basic
    @Column(name = "unit")
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "\"value\"")
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IngredientNutrition that = (IngredientNutrition) o;
        return Objects.equals(ingredientId, that.ingredientId) && Objects.equals(nutritionId, that.nutritionId) && Objects.equals(unit, that.unit) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, nutritionId, unit, value);
    }

    public class IngredientNutritionBuilder {
        private long ingredientId;
        private long nutritionId;
        private String unit;
        private BigDecimal value;

        public IngredientNutritionBuilder() {
        }

        public IngredientNutritionBuilder withIngredientId(long ingredientId) {
            this.ingredientId = ingredientId;
            return this;
        }

        public IngredientNutritionBuilder withNutritionId(long nutritionId) {
            this.nutritionId = nutritionId;
            return this;
        }

        public IngredientNutritionBuilder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public IngredientNutritionBuilder withValue(BigDecimal value) {
            this.value = value;
            return this;
        }

        public IngredientNutrition build() {
            IngredientNutrition ingredientNutrition = new IngredientNutrition();
            ingredientNutrition.setIngredientId(this.ingredientId);
            ingredientNutrition.setNutritionId(this.nutritionId);
            ingredientNutrition.setUnit(this.unit);
            ingredientNutrition.setValue(this.value);
            return ingredientNutrition;
        }
    }
}

