package at.ac.tuwien.sepr.groupphase.backend.entity;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Recipe_Step", schema = "PUBLIC", catalog = "DB")
public class RecipeStep {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "recipe_id")
    private long recipeId;
    @Basic
    @Column(name = "step_number")
    private Integer stepNumber;
    @Basic
    @Column(name = "step_description_id")
    private long stepDescriptionId;
    @Basic
    @Column(name = "step_recipe_id")
    private long stepRecipeId;

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

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public long getStepDescriptionId() {
        return stepDescriptionId;
    }

    public void setStepDescriptionId(long stepDescriptionId) {
        this.stepDescriptionId = stepDescriptionId;
    }

    public long getStepRecipeId() {
        return stepRecipeId;
    }

    public void setStepRecipeId(long stepRecipeId) {
        this.stepRecipeId = stepRecipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStep that = (RecipeStep) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(recipeId, that.recipeId) && Objects.equals(stepNumber, that.stepNumber) && Objects.equals(stepDescriptionId, that.stepDescriptionId) && Objects.equals(stepRecipeId, that.stepRecipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recipeId, stepNumber, stepDescriptionId, stepRecipeId);
    }
}