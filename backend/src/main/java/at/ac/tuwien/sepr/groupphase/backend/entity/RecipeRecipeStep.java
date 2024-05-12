package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

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
}