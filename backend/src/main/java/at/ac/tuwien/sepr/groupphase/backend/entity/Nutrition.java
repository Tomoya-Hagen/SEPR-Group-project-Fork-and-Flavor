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
import java.util.UUID;

@Entity
public class Nutrition {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nutrition nutrition = (Nutrition) o;
        return Objects.equals(id, nutrition.id) && Objects.equals(name, nutrition.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutrition_id",referencedColumnName = "id")
    private List<IngredientNutrition> ingredientNutritions;

    public class NutritionBuilder {
        private long id;
        private String name;

        public NutritionBuilder() {
        }

        public NutritionBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public NutritionBuilder withName(String name) {
            this.name = name;
            return this;
        }


        public Nutrition build() {
            Nutrition nutrition = new Nutrition();
            nutrition.setId(this.id);
            nutrition.setName(this.name);
            return nutrition;
        }
    }
}
