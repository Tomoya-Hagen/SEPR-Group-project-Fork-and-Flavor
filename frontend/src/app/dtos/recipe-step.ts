import { RecipeDetailDto } from "./recipe"

export interface RecipeStepDetailDto {
    id: number,
    name: string,
    stepNumber: number,
    expanded: boolean | false
}

export interface RecipeStepRecipeDetailDto extends RecipeStepDetailDto {
    recipe: RecipeDetailDto
}

export interface RecipeStepDescriptionDetailDto extends RecipeStepDetailDto{
    description: string
}
