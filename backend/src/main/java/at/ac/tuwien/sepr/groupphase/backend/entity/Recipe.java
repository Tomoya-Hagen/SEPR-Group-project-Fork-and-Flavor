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
import jakarta.persistence.OneToMany;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Recipe {
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

    @Basic
    @Column(name = "number_of_servings")
    private Short numberOfServings;

    @Basic
    @Column(name = "forked_from")
    private Long forkedFrom;

    @Basic
    @Column(name = "owner_id")
    private long ownerId;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "recipe_category",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @Basic
    @Column(name = "is_draft")
    private Boolean isDraft;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "favorite",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<ApplicationUser> favorites;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "recipe_recipe_book",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_book_id")
    )
    private List<RecipeBook> recipeBooks;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<Cooked> cooked;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<Rating> ratings;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeStep> recipeSteps;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeRecipeStep> recipeRecipeSteps;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeVerified> recipesVerified;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<WeeklyPlanner> weeklyPlanners;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeIngredient> ingredients;



    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "forked_from", referencedColumnName = "id")
    private List<Recipe> forkedfrom;

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }


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

    public Short getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Short numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public long getForkedFrom() {
        return forkedFrom;
    }

    public void setForkedFrom(Long forkedFrom) {
        this.forkedFrom = forkedFrom;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Boolean getDraft() {
        return isDraft;
    }

    public void setDraft(Boolean draft) {
        isDraft = draft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id)
            && Objects.equals(name, recipe.name)
            && Objects.equals(description, recipe.description)
            && Objects.equals(numberOfServings, recipe.numberOfServings)
            && Objects.equals(forkedFrom, recipe.forkedFrom) && Objects.equals(ownerId, recipe.ownerId) && Objects.equals(isDraft, recipe.isDraft);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, numberOfServings, forkedFrom, ownerId, isDraft);
    }
}
