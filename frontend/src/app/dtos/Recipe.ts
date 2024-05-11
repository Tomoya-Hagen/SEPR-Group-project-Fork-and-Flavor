import {Ingredient} from "./Ingredient";

export interface Recipe {
  name: string;
  description: string;
  servings: number;
  ownerid: number;
  ingredients: Ingredient[];
}
