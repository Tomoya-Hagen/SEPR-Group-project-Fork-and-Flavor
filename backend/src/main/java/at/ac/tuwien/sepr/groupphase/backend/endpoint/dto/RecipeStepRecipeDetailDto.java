package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class RecipeStepRecipeDetailDto extends RecipeStepDetailDto {
    private RecipeDetailDto recipe;

    public RecipeDetailDto getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeDetailDto recipe) {
        this.recipe = recipe;
    }
}
