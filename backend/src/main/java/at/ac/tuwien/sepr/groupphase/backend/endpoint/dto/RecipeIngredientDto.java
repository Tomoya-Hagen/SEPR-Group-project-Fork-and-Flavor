package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Objects;

public class RecipeIngredientDto {
    @NotNull(message = "ingredientId must not be null")
    private long ingredientId;
    @NotNull(message = "Amount must not be null")
    private BigDecimal amount;
    @NotNull(message = "String must not be null")
    private long unit;

    @NotNull(message = "ingredientid must not be null")
    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public long getUnit() {
        return unit;
    }

    public void setUnit(long unit) {
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
        RecipeIngredientDto that = (RecipeIngredientDto) o;
        return ingredientId == that.ingredientId && unit == that.unit && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, amount, unit);
    }

    public static final class RecipeIngredientDtoBuilder {
        private long ingredientId;
        private BigDecimal amount;
        private long unit;

        private RecipeIngredientDtoBuilder() {
        }

        public static RecipeIngredientDtoBuilder aRecipeIngredientDto() {
            return new RecipeIngredientDtoBuilder();
        }

        public RecipeIngredientDtoBuilder withIngredientId(long ingredientId) {
            this.ingredientId = ingredientId;
            return this;
        }

        public RecipeIngredientDtoBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public RecipeIngredientDtoBuilder withUnit(long unit) {
            this.unit = unit;
            return this;
        }

        public RecipeIngredientDto build() {
            RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto();
            recipeIngredientDto.setIngredientId(ingredientId);
            recipeIngredientDto.setAmount(amount);
            recipeIngredientDto.setUnit(unit);
            return recipeIngredientDto;
        }
    }
}

