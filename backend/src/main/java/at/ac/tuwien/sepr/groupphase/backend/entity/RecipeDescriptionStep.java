package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
}

