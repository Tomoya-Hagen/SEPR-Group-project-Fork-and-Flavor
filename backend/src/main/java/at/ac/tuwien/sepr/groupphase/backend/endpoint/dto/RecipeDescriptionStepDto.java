package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RecipeDescriptionStepDto extends RecipeStepDto{


    @NotNull(message = "description must not be null")
    private String description;

    @NotNull(message = "name must not be null")
    private String recipename;


    public  String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        RecipeDescriptionStepDto that = (RecipeDescriptionStepDto) o;
        return Objects.equals(description, that.description) && Objects.equals(recipename, that.recipename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, recipename);
    }


    public static final class RecipeDescriptionStepDtoBuilder {
        private String description;
        private String recipename;
        private int stepnumber;
        private String name;

        private RecipeDescriptionStepDtoBuilder() {
        }

        public static RecipeDescriptionStepDtoBuilder aRecipeDescriptionStepDto() {
            return new RecipeDescriptionStepDtoBuilder();
        }

        public RecipeDescriptionStepDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeDescriptionStepDtoBuilder withRecipename(String recipename) {
            this.recipename = recipename;
            return this;
        }

        public RecipeDescriptionStepDtoBuilder withStepnumber(int stepnumber) {
            this.stepnumber = stepnumber;
            return this;
        }

        public RecipeDescriptionStepDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeDescriptionStepDto build() {
            RecipeDescriptionStepDto recipeDescriptionStepDto = new RecipeDescriptionStepDto();
            recipeDescriptionStepDto.setDescription(description);
            recipeDescriptionStepDto.setStepnumber(stepnumber);
            recipeDescriptionStepDto.setName(name);
            recipeDescriptionStepDto.recipename = this.recipename;
            return recipeDescriptionStepDto;
        }
    }
}
