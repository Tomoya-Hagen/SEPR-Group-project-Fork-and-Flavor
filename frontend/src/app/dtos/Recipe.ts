import {Ingredient} from "./Ingredient";
import {Step} from "./Step";
import {Category} from "./Category";

export interface Recipe {
  name: string;
  description: string;
  servings: number;
  ownerId: number;
  ingredients: Ingredient[];
  steps: Step[]
  categories: Category[]
}

