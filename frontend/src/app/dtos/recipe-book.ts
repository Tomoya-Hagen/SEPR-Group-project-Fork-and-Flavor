import {userListDto} from "./user";
import {RecipeListDto} from "./recipe"

export interface RecipeBook {
  id: number,
  name: string,
  description: string,
  ownerId: number,
  users: userListDto[],
  recipes: RecipeListDto[]
}

export interface RecipeBookListDto {
  id: number,
  name: string,
  ownerId: number,
  description: string
}

export interface RecipeBookSearch {
  name?: string;
}
