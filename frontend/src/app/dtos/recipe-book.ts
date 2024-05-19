import {userListDto} from "../dtos/user";
import {Recipe} from "../dtos/recipe";
import {RecipeListDto} from "../dtos/recipe"

export interface RecipeBook {
    id: number,
    name: string,
    description: string,
    ownerId: number,
    users: userListDto[],
    recipes: Recipe[]
}

export interface RecipeBookCreateDto {
    id: number,
    name: string,
    description: string,
    ownerId: number,
    users: userListDto[],
    recipes: RecipeListDto[]
}