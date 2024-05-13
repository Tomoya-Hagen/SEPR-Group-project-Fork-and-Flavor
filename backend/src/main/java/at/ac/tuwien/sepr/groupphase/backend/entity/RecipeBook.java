package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Recipe_Book", schema = "PUBLIC", catalog = "DB")
public class RecipeBook {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "owner_id")
    private long ownerId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeBook that = (RecipeBook) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ownerId);
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_book_id", referencedColumnName = "id")
    private List<UserRecipeBook> userRecipeBooks;

    public void setUserRecipeBooks(List<UserRecipeBook> userRecipeBooks) {
        this.userRecipeBooks = userRecipeBooks;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_book_id", referencedColumnName = "id")
    private List<RecipeRecipeBook> recipeRecipeBooks;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "recipe_recipe_book",
        joinColumns = @JoinColumn(name = "recipe_book_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Recipe> recipes;

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
