package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(mappedBy = "allergens")
    private Set<Ingredient> ingredients = new HashSet<>();

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
