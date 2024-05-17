package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.List;

@Entity
public class Allergen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type", length = 1)
    private String type;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "ingredient_allergen",
        joinColumns = @JoinColumn(name = "allergen_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    // Getters and setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public static final class AllergenBuilder {
        private Long id;
        private String name;
        private String description;
        private String type;

        private AllergenBuilder() {
        }

        public static AllergenBuilder anAllergen() {
            return new AllergenBuilder();
        }

        public AllergenBuilder withId(Long id) {
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

        public AllergenBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public Allergen build() {
            Allergen allergen = new Allergen();
            allergen.setId(id);
            allergen.setName(name);
            allergen.setDescription(description);
            allergen.setType(type);
            return allergen;
        }
    }

}
