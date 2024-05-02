package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class IngredientAllergenPK implements Serializable {
    @Column(name = "ingredient_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ingredientId;
    @Column(name = "allergen_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long allergenId;

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public long getAllergenId() {
        return allergenId;
    }

    public void setAllergenId(long allergenId) {
        this.allergenId = allergenId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientAllergenPK that = (IngredientAllergenPK) o;
        return Objects.equals(ingredientId, that.ingredientId) && Objects.equals(allergenId, that.allergenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, allergenId);
    }
}
