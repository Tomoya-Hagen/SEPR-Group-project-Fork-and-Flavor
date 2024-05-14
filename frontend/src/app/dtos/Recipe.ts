import {Ingredient} from "./Ingredient";
import {Step} from "./Step";
import {Category} from "./Category";

export interface Recipe {
  name: string;
  description: string;
  servings: number;
  ownerid: number;
  ingredients: Ingredient[];
  steps: Step[]
  category: Category[]
}

