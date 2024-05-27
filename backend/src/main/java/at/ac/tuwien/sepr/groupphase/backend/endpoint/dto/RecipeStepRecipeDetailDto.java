package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class RecipeStepRecipeDetailDto extends RecipeStepDetailDto {
    private RecipeDetailDto recipe;

    public RecipeDetailDto getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeDetailDto recipe) {
        this.recipe = recipe;
    }

    public RecipeStepRecipeDetailDto(long id,
                                     String name,
                                     int stepNumber,
                                     RecipeDetailDto recipe) {
        super(id, name, stepNumber);
        this.recipe = recipe;
    }

    public RecipeStepRecipeDetailDto() {

    }
}
