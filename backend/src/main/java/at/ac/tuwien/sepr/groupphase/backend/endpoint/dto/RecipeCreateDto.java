package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

public class RecipeCreateDto {
    @NotNull(message = "Name must not be null")
    @Size(max = 100)
    private String name;
    @NotNull(message = "Description must not be null")
    @Size(max = 500)
    private String description;
    @NotNull(message = "Number of servings must not be null")
    private Short numberOfServings;
    @NotNull(message = "User must not be null")
    private int ownerId;
    @NotNull(message = "Recipe steps must not be null")
    private List<RecipeStepDto> recipeSteps;
    @NotNull(message = "Recipe categories must not be null")
    private List<RecipeCategoryDto> recipeCategories;
    @NotNull(message = "Recipe ingredients must not be null")
    private List<RecipeIngredientDto> recipeIngredients;

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

    public Short getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Short numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public  List<RecipeStepDto> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeStepDto> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public List<RecipeCategoryDto> getRecipeCategories() {
        return recipeCategories;
    }

    public void setRecipeCategories( List<RecipeCategoryDto> recipeCategories) {
        this.recipeCategories = recipeCategories;
    }

    public  List<RecipeIngredientDto> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients( List<RecipeIngredientDto> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeCreateDto that = (RecipeCreateDto) o;
        return Objects.equals(name, that.name)
            && Objects.equals(description, that.description)
            && Objects.equals(numberOfServings, that.numberOfServings)
            && Objects.equals(ownerId, that.ownerId)
            && Objects.equals(recipeSteps, that.recipeSteps)
            && Objects.equals(recipeCategories, that.recipeCategories)
            && Objects.equals(recipeIngredients, that.recipeIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, numberOfServings, ownerId, recipeSteps, recipeCategories, recipeIngredients);
    }


    public static final class RecipeCreateDtoBuilder {
        private String name;
        private String description;
        private Short numberOfServings;
        private int ownerId;
        private List<RecipeStepDto> recipeSteps;
        private List<RecipeCategoryDto> recipeCategories;
        private List<RecipeIngredientDto> recipeIngredients;

        private RecipeCreateDtoBuilder() {
        }

        public static RecipeCreateDtoBuilder aRecipeCreateDto() {
            return new RecipeCreateDtoBuilder();
        }

        public RecipeCreateDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeCreateDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeCreateDtoBuilder withNumberOfServings(Short numberOfServings) {
            this.numberOfServings = numberOfServings;
            return this;
        }

        public RecipeCreateDtoBuilder withOwnerId(int user) {
            this.ownerId = ownerId;
            return this;
        }

        public RecipeCreateDtoBuilder withRecipeSteps(List<RecipeStepDto> recipeSteps) {
            this.recipeSteps = recipeSteps;
            return this;
        }

        public RecipeCreateDtoBuilder withRecipeCategories(List<RecipeCategoryDto> recipeCategories) {
            this.recipeCategories = recipeCategories;
            return this;
        }

        public RecipeCreateDtoBuilder withRecipeIngredients(List<RecipeIngredientDto> recipeIngredients) {
            this.recipeIngredients = recipeIngredients;
            return this;
        }

        public RecipeCreateDto build() {
            RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
            recipeCreateDto.setName(name);
            recipeCreateDto.setDescription(description);
            recipeCreateDto.setNumberOfServings(numberOfServings);
            recipeCreateDto.setOwnerId(ownerId);
            recipeCreateDto.setRecipeSteps(recipeSteps);
            recipeCreateDto.setRecipeCategories(recipeCategories);
            recipeCreateDto.setRecipeIngredients(recipeIngredients);
            return recipeCreateDto;
        }
    }
}
