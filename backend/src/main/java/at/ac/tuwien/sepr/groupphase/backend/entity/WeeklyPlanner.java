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

import java.sql.Date;

@Entity
@Table(name = "Weekly_Planner", schema = "PUBLIC", catalog = "DB",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"cook_date", "daytime", "recipe_book_id"})}
)
public class WeeklyPlanner {

    public enum EatingTime {
        Frühstück,
        Mittagessen,
        Abendessen
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "cook_date")
    private Date date;
    @Basic
    @Column(name = "daytime")
    private EatingTime daytime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_book_id", nullable = false)
    private RecipeBook recipeBook;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EatingTime getDaytime() {
        return daytime;
    }

    public void setDaytime(EatingTime daytime) {
        this.daytime = daytime;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public RecipeBook getRecipeBook() {
        return recipeBook;
    }

    public void setRecipeBook(RecipeBook recipeBook) {
        this.recipeBook = recipeBook;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
