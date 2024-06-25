import {userDto, userListDto} from "./user";
import {RecipeListDto} from "./recipe"

export interface RecipeBookDetailDto {
  id?: number,
  name: string,
  description: string,
  ownerId: number,
  owner: userDto,
  users: userListDto[],
  recipes: RecipeListDto[],
  canBeMadeWeekPlan: boolean
}

export interface RecipeBook {
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
  users: userListDto[],
  recipes: RecipeListDto[]
}

export interface RecipeBookCreateDto {
  name: string,
  description: string,
  users: userListDto[],
  recipes: RecipeListDto[]
}


export interface RecipeBookSearch {
  name?: string;
}
