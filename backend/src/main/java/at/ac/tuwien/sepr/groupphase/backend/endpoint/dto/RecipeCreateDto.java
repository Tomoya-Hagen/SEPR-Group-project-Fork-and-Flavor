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
    @NotNull(message = "Recipe steps must not be null")
    private List<RecipeStepDto> recipeSteps;
    @NotNull(message = "Recipe categories must not be null")
    private List<RecipeCategoryDto> categories;
    @NotNull(message = "Recipe ingredients must not be null")
    private List<RecipeIngredientDto> ingredients;

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

    public List<RecipeStepDto> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeStepDto> steps) {
        this.recipeSteps = steps;
    }

    public List<RecipeCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<RecipeCategoryDto> categories) {
        this.categories = categories;
    }

    public List<RecipeIngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientDto> ingredients) {
        this.ingredients = ingredients;
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
            && Objects.equals(recipeSteps, that.recipeSteps)
            && Objects.equals(categories, that.categories)
            && Objects.equals(ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, numberOfServings, recipeSteps, categories, ingredients);
    }

    public RecipeCreateDto(String name, String description, Short numberOfServings, List<RecipeStepDto> recipeSteps, List<RecipeCategoryDto> categories, List<RecipeIngredientDto> ingredients) {
        this.name = name;
        this.description = description;
        this.numberOfServings = numberOfServings;
        this.recipeSteps = recipeSteps;
        this.categories = categories;
        this.ingredients = ingredients;
    }

    public RecipeCreateDto(RecipeCreateDto dto) {
        this.name = dto.name;
        this.description = dto.description;
        this.numberOfServings = dto.numberOfServings;
        this.recipeSteps = dto.recipeSteps;
        this.categories = dto.categories;
        this.ingredients = dto.ingredients;
    }

    public RecipeCreateDto() {}

    public static final class RecipeCreateDtoBuilder {
        private String name;
        private String description;
        private Short numberOfServings;
        private List<RecipeStepDto> recipeSteps;
        private List<RecipeCategoryDto> categories;
        private List<RecipeIngredientDto> ingredients;

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

        public RecipeCreateDtoBuilder withRecipeSteps(List<RecipeStepDto> recipeSteps) {
            this.recipeSteps = recipeSteps;
            return this;
        }

        public RecipeCreateDtoBuilder withCategories(List<RecipeCategoryDto> categories) {
            this.categories = categories;
            return this;
        }

        public RecipeCreateDtoBuilder withIngredients(List<RecipeIngredientDto> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeCreateDto build() {
            RecipeCreateDto recipeCreateDto = new RecipeCreateDto();
            recipeCreateDto.setName(name);
            recipeCreateDto.setDescription(description);
            recipeCreateDto.setNumberOfServings(numberOfServings);
            recipeCreateDto.setRecipeSteps(recipeSteps);
            recipeCreateDto.setCategories(categories);
            recipeCreateDto.setIngredients(ingredients);
            return recipeCreateDto;
        }
    }
}
