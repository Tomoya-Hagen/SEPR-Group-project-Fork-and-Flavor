package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeRecipeStep;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RecipeRecipeStepDto extends RecipeStepDto{

    @NotNull(message = "recipeId must not be null")
    private long recipeId;

    @NotNull(message = "name must not be null")
    private String recipename;


    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipename;
    }

    public void setRecipeName(String recipename) {
        this.recipename = recipename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeRecipeStepDto that = (RecipeRecipeStepDto) o;
        return Objects.equals(recipeId, that.recipeId) && Objects.equals(recipename, that.recipename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, recipename);
    }


    public static final class RecipeRecipeStepDtoBuilder {
        private long recipeId;
        private String recipename;
        private int stepnumber;
        private String name;

        private RecipeRecipeStepDtoBuilder() {
        }

        public static RecipeRecipeStepDtoBuilder aRecipeRecipeStepDto() {
            return new RecipeRecipeStepDtoBuilder();
        }

        public RecipeRecipeStepDtoBuilder withRecipeId(long recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public RecipeRecipeStepDtoBuilder withRecipeName(String recipename) {
            this.recipename = recipename;
            return this;
        }

        public RecipeRecipeStepDtoBuilder withStepnumber(int stepnumber) {
            this.stepnumber = stepnumber;
            return this;
        }

        public RecipeRecipeStepDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeRecipeStepDto build() {
            RecipeRecipeStepDto recipeRecipeStepDto = new RecipeRecipeStepDto();
            recipeRecipeStepDto.setRecipeId(recipeId);
            recipeRecipeStepDto.setRecipeName(recipename);
            recipeRecipeStepDto.setStepnumber(stepnumber);
            recipeRecipeStepDto.setName(name);
            return recipeRecipeStepDto;
        }
    }
}
