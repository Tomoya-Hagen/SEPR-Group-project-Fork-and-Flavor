import {AllergenDetailDto} from "./allergen"
import {CategoryDetailDto} from "./category"
import {Category} from "./category"
import {IngredientDetailDto} from "./ingredient"
import {RecipeStepDetailDto} from "./recipe-step"
import {NutritionDetailDto} from "./nutrition"
import {Step} from "./Step";
import {userDto} from "./user";

export interface RecipeListDto {
  id: number,
  name: string,
  rating: number,
  description: string
  image: string
}

export interface Recipe {
  name: string;
  description: string;
  numberOfServings: number;
  ingredients: IngredientDetailDto[];
  recipeSteps: Step[]
  categories: Category[]
}
export interface RecipeUpdateDto {
  id: number;
  name: string;
  description: string;
  numberOfServings: number;
  categories: Category[];
  recipeSteps: Step[];
  ingredients: IngredientDetailDto[];
}
export interface RecipeDetailDto {
  id: number,
  rating: number,
  name: string,
  description: string,
  numberOfServings: number,
  forkedFrom: RecipeListDto,
  ownerId: number,
  categories: CategoryDetailDto[],
  isDraft: boolean,
  recipeSteps: RecipeStepDetailDto[],
  ingredients: IngredientDetailDto[],
  allergens: AllergenDetailDto[],
  nutritions: NutritionDetailDto[],
  forkedRecipes: RecipeListDto[]
  isVerified: boolean,
  verifications: number,
}

export interface RecipeList {
  id: number,
  name: string,
}

export interface RecipeSearch {
  name?: string;
  categoryId?: number;
}


