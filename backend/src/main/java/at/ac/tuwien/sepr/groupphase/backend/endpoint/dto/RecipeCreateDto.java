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
    private Short servings;
    @NotNull(message = "User must not be null")
    private int ownerId;
    @NotNull(message = "Recipe steps must not be null")
    private List<RecipeStepDto> steps;
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

    public Short getServings() {
        return servings;
    }

    public void setServings(Short servings) {
        this.servings = servings;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public List<RecipeStepDto> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStepDto> steps) {
        this.steps = steps;
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
        return ownerId == that.ownerId && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(servings, that.servings) && Objects.equals(steps, that.steps) && Objects.equals(categories, that.categories) && Objects.equals(ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, servings, ownerId, steps, categories, ingredients);
    }


    public static final class RecipeCreateDtoBuilder {
        private String name;
        private String description;
        private Short servings;
        private int ownerId;
        private List<RecipeStepDto> steps;
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

        public RecipeCreateDtoBuilder withServings(Short servings) {
            this.servings = servings;
            return this;
        }

        public RecipeCreateDtoBuilder withOwnerId(int ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public RecipeCreateDtoBuilder withSteps(List<RecipeStepDto> steps) {
            this.steps = steps;
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
            recipeCreateDto.setServings(servings);
            recipeCreateDto.setOwnerId(ownerId);
            recipeCreateDto.setSteps(steps);
            recipeCreateDto.setCategories(categories);
            recipeCreateDto.setIngredients(ingredients);
            return recipeCreateDto;
        }
    }
}
