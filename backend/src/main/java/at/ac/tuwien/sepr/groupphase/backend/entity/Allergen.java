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
public class Allergen {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Allergen allergen = (Allergen) o;
        return Objects.equals(id, allergen.id) && Objects.equals(name, allergen.name) && Objects.equals(description, allergen.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "allergen_id", referencedColumnName = "id")
    private List<IngredientAllergen> ingredientAllergens;





    public static final class AllergenBuilder {
        private long id;
        private String name;
        private String description;
        private List<IngredientAllergen> ingredientAllergens;

        public AllergenBuilder() {
        }

        public AllergenBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public AllergenBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public AllergenBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public AllergenBuilder withIngredientAllergens(List<IngredientAllergen> ingredientAllergens) {
            this.ingredientAllergens = ingredientAllergens;
            return this;
        }

        public Allergen build() {
            Allergen allergen = new Allergen();
            allergen.setId(this.id);
            allergen.setName(this.name);
            allergen.setDescription(this.description);
            return allergen;
        }
    }
}


