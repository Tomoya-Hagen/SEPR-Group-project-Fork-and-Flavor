package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "User_Recipe_Book", schema = "PUBLIC", catalog = "DB")
public class UserRecipeBook {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_book_id", nullable = false)
    private RecipeBook recipeBook;

    public RecipeBook getRecipeBook() {
        return recipeBook;
    }

    public void setRecipeBook(RecipeBook recipeBook) {
        this.recipeBook = recipeBook;
    }

    @Basic
    @jakarta.persistence.Column(name = "permission")
    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRecipeBook that = (UserRecipeBook) o;
        return Objects.equals(user, that.user) && Objects.equals(recipeBook, that.recipeBook) && Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, recipeBook, permission);
    }
}
