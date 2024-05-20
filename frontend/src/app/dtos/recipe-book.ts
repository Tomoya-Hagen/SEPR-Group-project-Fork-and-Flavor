import {userListDto} from "../dtos/user";
import {RecipeListDto} from "../dtos/recipe"

export interface RecipeBook {
    id?: number,
    name: string,
    description: string,
    ownerId: number,
    users: userListDto[],
    recipes: RecipeListDto[]
}

export interface RecipeBookCreateDto {
    name: string,
    description: string,
    ownerId: number,
    users: userListDto[],
    recipes: RecipeListDto[]
}