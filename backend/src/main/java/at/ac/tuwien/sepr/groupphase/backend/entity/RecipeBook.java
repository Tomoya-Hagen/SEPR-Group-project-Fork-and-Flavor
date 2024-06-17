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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Recipe_Book", schema = "PUBLIC", catalog = "DB")
public class RecipeBook {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private ApplicationUser owner;

    @ManyToMany
    @JoinTable(
        name = "recipe_recipe_book",
        joinColumns = @JoinColumn(name = "recipe_book_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    )
    private List<Recipe> recipes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_book_id", referencedColumnName = "id")
    private List<WeeklyPlanner> weeklyPlanner;

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Long getOwnerId() {
        return owner.getId();
    }

    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeBook that = (RecipeBook) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(owner.getId(), that.owner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, owner.getId());
    }

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_recipe_book",
        joinColumns = {@JoinColumn(name = "recipe_book_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<ApplicationUser> editors = new ArrayList<>();

    public List<ApplicationUser> getEditors() {
        return editors;
    }

    public void setEditors(List<ApplicationUser> editors) {
        this.editors = editors;
    }

    public static final class RecipeBookBuilder {
        private long id;
        private String name;
        private String description;
        private ApplicationUser owner;
        private List<Recipe> recipes;
        private List<WeeklyPlanner> weeklyPlanner;
        private List<ApplicationUser> editors;

        private RecipeBookBuilder() {
        }

        public static RecipeBookBuilder aRecipeBook() {
            return new RecipeBookBuilder();
        }

        public RecipeBookBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public RecipeBookBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeBookBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeBookBuilder withOwner(ApplicationUser owner) {
            this.owner = owner;
            return this;
        }

        public RecipeBookBuilder withRecipes(List<Recipe> recipes) {
            this.recipes = recipes;
            return this;
        }

        public RecipeBookBuilder withWeeklyPlanner(List<WeeklyPlanner> weeklyPlanner) {
            this.weeklyPlanner = weeklyPlanner;
            return this;
        }

        public RecipeBookBuilder withEditors(List<ApplicationUser> editors) {
            this.editors = editors;
            return this;
        }

        public RecipeBook build() {
            RecipeBook recipeBook = new RecipeBook();
            recipeBook.setId(id);
            recipeBook.setName(name);
            recipeBook.setDescription(description);
            recipeBook.setRecipes(recipes);
            recipeBook.setEditors(editors);
            recipeBook.weeklyPlanner = this.weeklyPlanner;
            recipeBook.owner = this.owner;
            return recipeBook;
        }
    }
}
