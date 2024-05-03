package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Objects;

@Entity
public class Ingredient {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private List<IngredientAllergen> ingredientAllergens;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private List<IngredientNutrition> ingredientNutritions;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private List<RecipeIngredient> recipeIngredients;




    public class IngredientBuilder {
        private long id;
        private String name;

        public IngredientBuilder() {
        }

        public IngredientBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public IngredientBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public Ingredient build() {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(this.id);
            ingredient.setName(this.name);
            return ingredient;
        }
    }
}

