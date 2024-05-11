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

    @Basic
    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "number_of_servings")
    private Short numberOfServings;

    public Short getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Short numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    @Basic
    @Column(name = "forked_from")
    private Long forkedFrom;

    public Long getForkedFrom() {
        return forkedFrom;
    }

    public void setForkedFrom(Long forkedFrom) {
        this.forkedFrom = forkedFrom;
    }

    @Basic
    @Column(name = "owner_id")
    private long ownerId;

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "recipe_categories",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Basic
    @Column(name = "is_draft")
    private Boolean isDraft;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeRecipeBook> recipeRecipeBooks;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<Favorite> favorites;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<Cooked> cooked;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<Rating> ratings;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeStep> recipeSteps;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeRecipeStep> recipeRecipeSteps;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeVerified> recipesVerified;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<WeeklyPlanner> weeklyPlanners;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeIngredient> recipeIngredients;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "forked_from", referencedColumnName = "id")
    private List<Recipe> forkedfrom;

    public List<RecipeRecipeBook> getRecipeRecipeBooks() {
        return recipeRecipeBooks;
    }

    public void setRecipeRecipeBooks(List<RecipeRecipeBook> recipeRecipeBooks) {
        this.recipeRecipeBooks = recipeRecipeBooks;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Cooked> getCooked() {
        return cooked;
    }

    public void setCooked(List<Cooked> cooked) {
        this.cooked = cooked;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public List<RecipeRecipeStep> getRecipeRecipeSteps() {
        return recipeRecipeSteps;
    }

    public void setRecipeRecipeSteps(List<RecipeRecipeStep> recipeRecipeSteps) {
        this.recipeRecipeSteps = recipeRecipeSteps;
    }

    public List<RecipeVerified> getRecipesVerified() {
        return recipesVerified;
    }

    public void setRecipesVerified(List<RecipeVerified> recipesVerified) {
        this.recipesVerified = recipesVerified;
    }

    public List<WeeklyPlanner> getWeeklyPlanners() {
        return weeklyPlanners;
    }

    public void setWeeklyPlanners(List<WeeklyPlanner> weeklyPlanners) {
        this.weeklyPlanners = weeklyPlanners;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<Recipe> getForkedfrom() {
        return forkedfrom;
    }

    public void setForkedfrom(List<Recipe> forkedfrom) {
        this.forkedfrom = forkedfrom;
    }

    public static final class RecipeBuilder {
        private long id;
        private String name;
        private String description;
        private Short numberOfServings;
        private Long forkedFrom;
        private long ownerId;
        private Boolean isDraft;
        private List<RecipeRecipeBook> recipeRecipeBooks;
        private List<Favorite> favorites;
        private List<Cooked> cooked;
        private List<Rating> ratings;
        private List<RecipeStep> recipeSteps;
        private List<RecipeRecipeStep> recipeRecipeSteps;
        private List<RecipeVerified> recipesVerified;
        private List<WeeklyPlanner> weeklyPlanners;
        private List<RecipeIngredient> recipeIngredients;
        private List<Recipe> forkedfrom;

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

        public RecipeBuilder withForkedFrom(Long forkedFrom) {
            this.forkedFrom = forkedFrom;
            return this;
        }

        public RecipeBuilder withOwnerId(long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public RecipeBuilder withIsDraft(Boolean isDraft) {
            this.isDraft = isDraft;
            return this;
        }

        public RecipeBuilder withRecipeRecipeBooks(List<RecipeRecipeBook> recipeRecipeBooks) {
            this.recipeRecipeBooks = recipeRecipeBooks;
            return this;
        }

        public RecipeBuilder withFavorites(List<Favorite> favorites) {
            this.favorites = favorites;
            return this;
        }

        public RecipeBuilder withCooked(List<Cooked> cooked) {
            this.cooked = cooked;
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

        public RecipeBuilder withRecipeRecipeSteps(List<RecipeRecipeStep> recipeRecipeSteps) {
            this.recipeRecipeSteps = recipeRecipeSteps;
            return this;
        }

        public RecipeBuilder withRecipesVerified(List<RecipeVerified> recipesVerified) {
            this.recipesVerified = recipesVerified;
            return this;
        }

        public RecipeBuilder withWeeklyPlanners(List<WeeklyPlanner> weeklyPlanners) {
            this.weeklyPlanners = weeklyPlanners;
            return this;
        }

        public RecipeBuilder withRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
            this.recipeIngredients = recipeIngredients;
            return this;
        }

        public RecipeBuilder withForkedfrom(List<Recipe> forkedfrom) {
            this.forkedfrom = forkedfrom;
            return this;
        }

        public Recipe build() {
            Recipe recipe = new Recipe();
            recipe.setId(id);
            recipe.setName(name);
            recipe.setDescription(description);
            recipe.setNumberOfServings(numberOfServings);
            recipe.setForkedFrom(forkedFrom);
            recipe.setOwnerId(ownerId);
            recipe.recipeSteps = this.recipeSteps;
            recipe.favorites = this.favorites;
            recipe.cooked = this.cooked;
            recipe.recipeRecipeSteps = this.recipeRecipeSteps;
            recipe.isDraft = this.isDraft;
            recipe.recipeRecipeBooks = this.recipeRecipeBooks;
            recipe.ratings = this.ratings;
            recipe.recipeIngredients = this.recipeIngredients;
            recipe.forkedfrom = this.forkedfrom;
            recipe.recipesVerified = this.recipesVerified;
            recipe.weeklyPlanners = this.weeklyPlanners;
            return recipe;
        }
    }
}
