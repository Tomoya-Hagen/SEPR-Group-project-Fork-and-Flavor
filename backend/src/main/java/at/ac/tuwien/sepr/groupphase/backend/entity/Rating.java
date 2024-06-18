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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "rating", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "recipe_id"})
})
public class Rating {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
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

    @Basic
    @Column(name = "cost")
    private BigDecimal cost;

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "taste")
    private BigInteger taste;

    public BigInteger getTaste() {
        return taste;
    }

    public void setTaste(BigInteger taste) {
        this.taste = taste;
    }

    @Basic
    @Column(name = "ease_of_prep")
    private BigInteger easeOfPrep;

    public BigInteger getEaseOfPrep() {
        return easeOfPrep;
    }

    public void setEaseOfPrep(BigInteger easeOfPrep) {
        this.easeOfPrep = easeOfPrep;
    }

    @Basic
    @Column(name = "review")
    private String review;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String toEmailString() {
        return "Benutzer: " + user.getUsername() + "\n"
            + "Geschmack: " + taste + "\n"
            + "Einfachheit: " + easeOfPrep + "\n"
            + "Kosten: " + cost + "\n"
            + "Review: " + review + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rating rating = (Rating) o;
        return Objects.equals(user, rating.user)
            && Objects.equals(recipe, rating.recipe)
            && Objects.equals(cost, rating.cost)
            && Objects.equals(taste, rating.taste)
            && Objects.equals(easeOfPrep, rating.easeOfPrep)
            && Objects.equals(review, rating.review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, recipe, cost, taste, easeOfPrep, review);
    }
}