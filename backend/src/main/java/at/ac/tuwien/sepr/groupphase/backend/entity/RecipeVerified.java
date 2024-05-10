package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_id", nullable = false)
    private Verified verified;
    public Verified getVerified() {
        return verified;
    }

    public void setVerified(Verified verified) {
        this.verified = verified;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
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
        return Objects.equals(id, that.id) && Objects.equals(recipe, that.recipe) && Objects.equals(verified, that.verified) && Objects.equals(user, that.user) && Objects.equals(isExternal, that.isExternal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipe, verified, user, isExternal);
    }
}