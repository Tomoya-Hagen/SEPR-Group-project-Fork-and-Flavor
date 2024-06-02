package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class RecipeDescriptionStep extends RecipeStep {
    @Basic
    @Column(name = "description", length = 10000)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecipeDescriptionStep() {
    }

    public RecipeDescriptionStep(String name, String description, Recipe recipe, int stepNumber) {
        super(name, recipe, stepNumber);
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
        RecipeDescriptionStep that = (RecipeDescriptionStep) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), description);
    }

    public static final class RecipeDescriptionStepBuilder {
        private String description;
        private long id;
        private String name;
        private Recipe recipe;
        private Integer stepNumber;

        private RecipeDescriptionStepBuilder() {
        }

        public static RecipeDescriptionStepBuilder aRecipeDescriptionStep() {
            return new RecipeDescriptionStepBuilder();
        }

        public RecipeDescriptionStepBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeDescriptionStepBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public RecipeDescriptionStepBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeDescriptionStepBuilder withRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public RecipeDescriptionStepBuilder withStepNumber(Integer stepNumber) {
            this.stepNumber = stepNumber;
            return this;
        }

        public RecipeDescriptionStep build() {
            RecipeDescriptionStep recipeDescriptionStep = new RecipeDescriptionStep();
            recipeDescriptionStep.setDescription(description);
            recipeDescriptionStep.setId(id);
            recipeDescriptionStep.setName(name);
            recipeDescriptionStep.setRecipe(recipe);
            recipeDescriptionStep.setStepNumber(stepNumber);
            return recipeDescriptionStep;
        }
    }
}

