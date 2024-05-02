package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "User_Recipe_Book", schema = "PUBLIC", catalog = "DB")
public class UserRecipeBook {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "user_id")
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "recipe_book_id")
    private long recipeBookId;

    public long getRecipeBookId() {
        return recipeBookId;
    }

    public void setRecipeBookId(long recipeBookId) {
        this.recipeBookId = recipeBookId;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecipeBook that = (UserRecipeBook) o;
        return Objects.equals(userId, that.userId) && Objects.equals(recipeBookId, that.recipeBookId) && Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recipeBookId, permission);
    }
}
