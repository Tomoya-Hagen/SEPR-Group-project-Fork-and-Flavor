export interface RecipeStepDetailDto {
    id: number,
    name: string,
    stepNumber: number
}

export interface RecipeStepRecipeDetailDto extends RecipeStepDetailDto {
    recipe: RecipeStepDetailDto
}

export interface RecipeStepDescriptionDetailDto extends RecipeStepDetailDto{
    description: string
}