package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Objects;

public class RecipeIngredientDto {

    @NotNull(message = "ingredientId must not be null")
    private long id;

    @NotNull(message = "amount must not be null")
    private BigDecimal amount;

    @NotNull(message = "unit must not be null")
    private String unit;

    public RecipeIngredientDto(long id, BigDecimal amount, String unit) {
        this.id = id;
        this.amount = amount;
        this.unit = unit;
    }

    public RecipeIngredientDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
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
        return id == that.id && Objects.equals(unit, that.unit) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, unit);
    }


    public static final class RecipeIngredientDtoBuilder {
        private long id;
        private BigDecimal amount;
        private String unit;

        private RecipeIngredientDtoBuilder() {
        }

        public static RecipeIngredientDtoBuilder aRecipeIngredientDto() {
            return new RecipeIngredientDtoBuilder();
        }

        public RecipeIngredientDtoBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public RecipeIngredientDtoBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public RecipeIngredientDtoBuilder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public RecipeIngredientDto build() {
            RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto();
            recipeIngredientDto.setId(id);
            recipeIngredientDto.setAmount(amount);
            recipeIngredientDto.setUnit(unit);
            return recipeIngredientDto;
        }
    }
}

