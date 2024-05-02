package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Recipe {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "number_of_servings")
    private Short numberOfServings;

    public Short getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Short numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    @Basic
    @Column(name = "forked_from")
    private long forkedFrom;

    public long getForkedFrom() {
        return forkedFrom;
    }

    public void setForkedFrom(long forkedFrom) {
        this.forkedFrom = forkedFrom;
    }

    @Basic
    @Column(name = "owner_id")
    private long ownerId;

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Basic
    @Column(name = "is_draft")
    private Boolean isDraft;

    public Boolean getDraft() {
        return isDraft;
    }

    public void setDraft(Boolean draft) {
        isDraft = draft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) && Objects.equals(name, recipe.name) && Objects.equals(description, recipe.description) && Objects.equals(numberOfServings, recipe.numberOfServings) && Objects.equals(forkedFrom, recipe.forkedFrom) && Objects.equals(ownerId, recipe.ownerId) && Objects.equals(isDraft, recipe.isDraft);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, numberOfServings, forkedFrom, ownerId, isDraft);
    }
}
