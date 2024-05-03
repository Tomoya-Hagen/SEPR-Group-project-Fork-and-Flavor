package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Ingredient_Allergen", schema = "PUBLIC", catalog = "DB")
@IdClass(IngredientAllergenPK.class)
public class IngredientAllergen {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ingredient_id")
    private long ingredientId;

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "allergen_id")
    private long allergenId;

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
        IngredientAllergen that = (IngredientAllergen) o;
        return Objects.equals(ingredientId, that.ingredientId) && Objects.equals(allergenId, that.allergenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, allergenId);
    }

    public class IngredientAllergenBuilder {
        private long ingredientId;
        private long allergenId;

        public IngredientAllergenBuilder() {
        }

        public IngredientAllergenBuilder withIngredientId(long ingredientId) {
            this.ingredientId = ingredientId;
            return this;
        }

        public IngredientAllergenBuilder withAllergenId(long allergenId) {
            this.allergenId = allergenId;
            return this;
        }

        public IngredientAllergen build() {
            IngredientAllergen ingredientAllergen = new IngredientAllergen();
            ingredientAllergen.setIngredientId(this.ingredientId);
            ingredientAllergen.setAllergenId(this.allergenId);
            return ingredientAllergen;
        }
    }
}
