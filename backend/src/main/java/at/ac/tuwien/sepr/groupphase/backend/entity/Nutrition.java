package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "unit", nullable = false)
    private String unit;

    // Getters and Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutrition_id", referencedColumnName = "id")
    private List<IngredientNutrition> ingredients = new ArrayList<>();

    public void setIngredients(List<IngredientNutrition> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientNutrition> getIngredients() {
        return ingredients;

    public static final class NutritionBuilder {
        private Long id;
        private String name;
        private String description;
        private String unit;

        private NutritionBuilder() {
        }

        public static NutritionBuilder aNutrition() {
            return new NutritionBuilder();
        }

        public NutritionBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public NutritionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public NutritionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public NutritionBuilder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public Nutrition build() {
            Nutrition nutrition = new Nutrition();
            nutrition.setId(id);
            nutrition.setName(name);
            nutrition.setDescription(description);
            nutrition.setUnit(unit);
            return nutrition;
        }
    }
}
