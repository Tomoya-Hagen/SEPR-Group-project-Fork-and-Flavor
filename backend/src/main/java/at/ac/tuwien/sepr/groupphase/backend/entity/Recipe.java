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
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DynamicUpdate
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forked_from")
    private Recipe forkedFrom;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private ApplicationUser owner;

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
        name = "recipe_recipe_book",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_book_id")
    )
    private List<RecipeBook> recipeBooks;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<Rating> ratings;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeStep> recipeSteps;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_recipe_id", referencedColumnName = "id")
    private List<RecipeStep> recipeRecipeSteps = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeVerified> recipesVerified = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<WeeklyPlanner> weeklyPlanner = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeIngredient> ingredients;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "forked_from", referencedColumnName = "id")
    private List<Recipe> recipesForkedFromThis = new ArrayList<>();

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

    public Recipe getForkedFrom() {
        return forkedFrom;
    }

    public void setForkedFrom(Recipe forkedFrom) {
        this.forkedFrom = forkedFrom;
    }

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean draft) {
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
            && Objects.equals(forkedFrom, recipe.forkedFrom) && Objects.equals(owner, recipe.owner) && Objects.equals(isDraft, recipe.isDraft);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, numberOfServings, forkedFrom, owner, isDraft);
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public long getOwnerId() {
        return owner.getId();
    }

    public String toStringSmall() {
        return "Recipe(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", numberOfServings=" + this.getNumberOfServings() + ", owner=" + this.getOwner().getUsername() + ")";
    }

    public static final class RecipeBuilder {
        private long id;
        private String name;
        private String description;
        private Short numberOfServings;
        private Recipe forkedFrom;
        private ApplicationUser owner;
        private List<Category> categories;
        private Boolean isDraft;
        private List<RecipeBook> recipeBooks;
        private List<Rating> ratings;
        private List<RecipeStep> recipeSteps;
        private List<RecipeStep> recipeRecipeSteps;
        private List<RecipeVerified> recipesVerified;
        private List<WeeklyPlanner> weeklyPlanner;
        private List<RecipeIngredient> ingredients;
        private List<Recipe> recipesForkedFromThis;

        private RecipeBuilder() {
        }

        public static RecipeBuilder aRecipe() {
            return new RecipeBuilder();
        }

        public RecipeBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public RecipeBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeBuilder withNumberOfServings(Short numberOfServings) {
            this.numberOfServings = numberOfServings;
            return this;
        }

        public RecipeBuilder withForkedFrom(Recipe forkedFrom) {
            if (forkedFrom == null) {
                return this;
            }
            this.forkedFrom = forkedFrom;
            return this;
        }

        public RecipeBuilder withOwner(ApplicationUser owner) {
            this.owner = owner;
            return this;
        }

        public RecipeBuilder withCategories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        public RecipeBuilder withIsDraft(Boolean isDraft) {
            this.isDraft = isDraft;
            return this;
        }

        public RecipeBuilder withRecipeBooks(List<RecipeBook> recipeBooks) {
            this.recipeBooks = recipeBooks;
            return this;
        }

        public RecipeBuilder withRatings(List<Rating> ratings) {
            this.ratings = ratings;
            return this;
        }

        public RecipeBuilder withRecipeSteps(List<RecipeStep> recipeSteps) {
            this.recipeSteps = recipeSteps;
            return this;
        }

        public RecipeBuilder withRecipeRecipeSteps(List<RecipeStep> recipeRecipeSteps) {
            this.recipeRecipeSteps = recipeRecipeSteps;
            return this;
        }

        public RecipeBuilder withRecipesVerified(List<RecipeVerified> recipesVerified) {
            this.recipesVerified = recipesVerified;
            return this;
        }

        public RecipeBuilder withWeeklyPlanner(List<WeeklyPlanner> weeklyPlanner) {
            this.weeklyPlanner = weeklyPlanner;
            return this;
        }

        public RecipeBuilder withIngredients(List<RecipeIngredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder withRecipesForkedFromThis(List<Recipe> recipesForkedFromThis) {
            this.recipesForkedFromThis = recipesForkedFromThis;
            return this;
        }

        public Recipe build() {
            Recipe recipe = new Recipe();
            recipe.setId(id);
            recipe.setName(name);
            recipe.setDescription(description);
            recipe.setNumberOfServings(numberOfServings);
            recipe.setForkedFrom(forkedFrom);
            recipe.setOwner(owner);
            recipe.setCategories(categories);
            recipe.setIsDraft(isDraft);
            recipe.setRecipeSteps(recipeSteps);
            recipe.setIngredients(ingredients);
            recipe.recipesForkedFromThis = this.recipesForkedFromThis;
            recipe.recipeRecipeSteps = this.recipeRecipeSteps;
            recipe.ratings = this.ratings;
            recipe.recipeBooks = this.recipeBooks;
            recipe.weeklyPlanner = this.weeklyPlanner;
            recipe.recipesVerified = this.recipesVerified;
            return recipe;
        }
    }
}
