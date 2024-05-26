package at.ac.tuwien.sepr.groupphase.backend.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@IdClass(Ratingpk.class)
public class Rating {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rating rating = (Rating) o;
        return Objects.equals(userId, rating.userId)
            && Objects.equals(recipeId, rating.recipeId)
            && Objects.equals(cost, rating.cost)
            && Objects.equals(taste, rating.taste)
            && Objects.equals(easeOfPrep, rating.easeOfPrep)
            && Objects.equals(review, rating.review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recipeId, cost, taste, easeOfPrep, review);
    }
}