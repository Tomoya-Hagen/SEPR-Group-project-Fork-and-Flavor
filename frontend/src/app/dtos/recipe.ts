import { AllergenDetailDto } from "./allergen"
import { CategoryDetailDto } from "./category"
import { IngredientDetailDto } from "./ingredient"
import { RecipeStepDetailDto } from "./recipe-step"
import { NutritionDetailDto } from "./nutrition"

export interface RecipeListDto {
    id: number,
    name: string,
    rating: number,
    description: string
    image: string
}

export interface RecipeDetailDto {
    id: number,
    rating: number,
    name: string,
    description: string,
    numberOfServings: number,
    forkedFromId: number,
    ownerId: number,
    categories: CategoryDetailDto[],
    isDraft: boolean,
    recipeSteps: RecipeStepDetailDto[],
    ingredients: IngredientDetailDto[],
    allergens: AllergenDetailDto[],
    nutritions: NutritionDetailDto[]
}

export interface RecipeList{
  id: number,
  name: string,
}

export interface RecipeSearch {
  name?: string;
}
