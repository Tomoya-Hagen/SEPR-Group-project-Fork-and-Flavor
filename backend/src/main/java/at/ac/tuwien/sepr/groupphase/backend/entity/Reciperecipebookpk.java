package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class Reciperecipebookpk implements Serializable {
    @Column(name = "recipe_book_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeBookId;
    @Column(name = "recipe_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeId;

    public long getRecipeBookId() {
        return recipeBookId;
    }

    public void setRecipeBookId(long recipeBookId) {
        this.recipeBookId = recipeBookId;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reciperecipebookpk that = (Reciperecipebookpk) o;
        return Objects.equals(recipeBookId, that.recipeBookId) && Objects.equals(recipeId, that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeBookId, recipeId);
    }
}