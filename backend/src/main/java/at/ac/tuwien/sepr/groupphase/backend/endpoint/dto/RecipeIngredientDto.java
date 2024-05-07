package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class RecipeIngredientDto {
    @NotNull(message = "Id must not be null")
    private int id;
    @NotNull(message = "Amount must not be null")
    private int amount;
    @NotNull(message = "String must not be null")
    private String unit;



    public int getId() {
        return id;
    }

    public void setId( int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount( int amount) {
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
        return id == that.id && amount == that.amount && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, unit);
    }


    public static final class RecipeIngredientDtoBuilder {
        private int id;
        private int amount;
        private String unit;

        private RecipeIngredientDtoBuilder() {
        }

        public static RecipeIngredientDtoBuilder aRecipeIngredientDto() {
            return new RecipeIngredientDtoBuilder();
        }

        public RecipeIngredientDtoBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public RecipeIngredientDtoBuilder withAmount(int amount) {
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

