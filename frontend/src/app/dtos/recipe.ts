export interface Recipe {
  id: number,
  name: string,
  description: string,
}

export interface RecipeList{
  id: number,
  name: string,
}


export interface RecipeSearch {
  name?: string;
}
