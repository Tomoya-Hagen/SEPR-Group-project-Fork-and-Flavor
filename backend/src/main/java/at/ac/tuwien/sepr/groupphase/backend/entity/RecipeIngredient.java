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
import java.util.Objects;

@Entity
@Table(name = "Recipe_Ingredient", schema = "PUBLIC", catalog = "DB",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"})})
public class RecipeIngredient {
    public enum Unit {
        mg,
        g,
        L,
        el,
        tl,
        ml,
        Priese,
        Stück,
        Dose,
        Packung,
        Zehe,
        Flasche,
        Becher,
        Tasse,
        EMPTY
    }

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, BigDecimal amount, Unit unit) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.unit = unit;
        this.amount = amount;
    }

    public RecipeIngredient() {

    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public long getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Basic
    @Column(name = "amount")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        if (amount == null) {
            return new BigDecimal(0);
        }
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "unit")
    private Unit unit;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeIngredient that = (RecipeIngredient) o;
        return Objects.equals(recipe, that.recipe) && Objects.equals(ingredient, that.ingredient) && Objects.equals(amount, that.amount) && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, ingredient, amount, unit);
    }

    public static Unit getUnitFromString(String input) {
        try {
            return Unit.valueOf(input);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String toString() {
        return "RecipeIngredient(id=" + this.getId() + ", recipe=" + this.getRecipe() + ", ingredient=" + this.getIngredient() + ", amount=" + this.getAmount() + ", unit=" + this.getUnit() + ")";
    }

    public static final class RecipeIngredientBuilder {
        private long id;
        private Recipe recipe;
        private Ingredient ingredient;
        private BigDecimal amount;
        private Unit unit;

        private RecipeIngredientBuilder() {
        }

        public static RecipeIngredientBuilder aRecipeIngredient() {
            return new RecipeIngredientBuilder();
        }

        public RecipeIngredientBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public RecipeIngredientBuilder withRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public RecipeIngredientBuilder withIngredient(Ingredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public RecipeIngredientBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public RecipeIngredientBuilder withUnit(Unit unit) {
            this.unit = unit;
            return this;
        }

        public RecipeIngredient build() {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setAmount(amount);
            recipeIngredient.setUnit(unit);
            recipeIngredient.id = this.id;
            return recipeIngredient;
        }
    }
}
