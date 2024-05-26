package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "Recipe_Verified", schema = "PUBLIC", catalog = "DB")
public class RecipeVerified {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @jakarta.persistence.Column(name = "recipe_id")
    private long recipeId;

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @Basic
    @jakarta.persistence.Column(name = "verified_id")
    private long verifiedId;

    public long getVerifiedId() {
        return verifiedId;
    }

    public void setVerifiedId(long verifiedId) {
        this.verifiedId = verifiedId;
    }

    @Basic
    @jakarta.persistence.Column(name = "user_id")
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @jakarta.persistence.Column(name = "is_external")
    private Boolean isExternal;

    public Boolean getExternal() {
        return isExternal;
    }

    public void setExternal(Boolean external) {
        isExternal = external;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeVerified that = (RecipeVerified) o;
        return Objects.equals(id, that.id) && Objects.equals(recipeId, that.recipeId) && Objects.equals(verifiedId, that.verifiedId) && Objects.equals(userId, that.userId) && Objects.equals(isExternal, that.isExternal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, verifiedId, userId, isExternal);
    }
}