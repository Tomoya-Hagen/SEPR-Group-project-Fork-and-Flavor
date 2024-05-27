import {userListDto} from "./user";
import {RecipeListDto} from "./recipe"
import {UserListDto} from "../dtos/user";
import {RecipeListDto} from "../dtos/recipe"

export interface RecipeBookDetailDto {
  id?: number,
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
  description: string,
  users: UserListDto[],
  recipes: RecipeListDto[]
}

export interface RecipeBookSearch {
  name?: string;
}
