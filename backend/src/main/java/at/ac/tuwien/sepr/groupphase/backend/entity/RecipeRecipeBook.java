package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Recipe_Recipe_Book", schema = "PUBLIC", catalog = "DB")
@IdClass(RecipeRecipeBookPK.class)
public class RecipeRecipeBook {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "recipe_book_id")
    private long recipeBookId;

    public long getRecipeBookId() {
        return recipeBookId;
    }

    public void setRecipeBookId(long recipeBookId) {
        this.recipeBookId = recipeBookId;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "recipe_id")
    private long recipeId;

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeRecipeBook that = (RecipeRecipeBook) o;
        return Objects.equals(recipeBookId, that.recipeBookId) && Objects.equals(recipeId, that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeBookId, recipeId);
    }
}
