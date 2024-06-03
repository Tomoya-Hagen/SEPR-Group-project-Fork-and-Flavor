package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

@Entity
public class RecipeRecipeStep extends RecipeStep {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_recipe_id")
    private Recipe recipeRecipe;


    public Recipe getRecipeRecipe() {
        return recipeRecipe;
    }

    public void setRecipeRecipe(Recipe recipe) {
        this.recipeRecipe = recipe;
    }

    public RecipeRecipeStep(String name, Recipe recipe, int stepNumber, Recipe recipeRecipe) {
        super(name, recipe, stepNumber);
        this.recipeRecipe = recipeRecipe;
    }

    public RecipeRecipeStep() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeRecipeStep that = (RecipeRecipeStep) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(recipeRecipe, that.recipeRecipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), recipeRecipe);
    }

    public static final class RecipeRecipeStepBuilder {
        private Recipe recipeRecipe;
        private long id;
        private String name;
        private Recipe recipe;
        private Integer stepNumber;

        private RecipeRecipeStepBuilder() {
        }

        public static RecipeRecipeStepBuilder aRecipeRecipeStep() {
            return new RecipeRecipeStepBuilder();
        }

        public RecipeRecipeStepBuilder withRecipeRecipe(Recipe recipeRecipe) {
            this.recipeRecipe = recipeRecipe;
            return this;
        }

        public RecipeRecipeStepBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public RecipeRecipeStepBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeRecipeStepBuilder withRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public RecipeRecipeStepBuilder withStepNumber(Integer stepNumber) {
            this.stepNumber = stepNumber;
            return this;
        }

        public RecipeRecipeStep build() {
            RecipeRecipeStep recipeRecipeStep = new RecipeRecipeStep();
            recipeRecipeStep.setRecipeRecipe(recipeRecipe);
            recipeRecipeStep.setId(id);
            recipeRecipeStep.setName(name);
            recipeRecipeStep.setRecipe(recipe);
            recipeRecipeStep.setStepNumber(stepNumber);
            return recipeRecipeStep;
        }
    }
}