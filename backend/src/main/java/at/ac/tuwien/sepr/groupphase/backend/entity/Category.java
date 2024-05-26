package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Basic;
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
import java.util.Objects;

@Entity
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "type")
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name) && Objects.equals(type, category.type);
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "recipe_category",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Recipe> recipes;

    public static final class CategoryBuilder {
        private long id;
        private String name;
        private String type;
        private List<Recipe> recipes;

        private CategoryBuilder() {
        }

        public static CategoryBuilder aCategory() {
            return new CategoryBuilder();
        }

        public CategoryBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public CategoryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public CategoryBuilder withRecipes(List<Recipe> recipes) {
            this.recipes = recipes;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.setId(id);
            category.setName(name);
            category.setType(type);
            category.setRecipes(recipes);
            return category;
        }
    }

}
