import { userDto } from "./user";

export interface RatingListDto {
    user: userDto,
    cost: number,
    taste: number,
    easeOfPrep: number,
    review: String,
    recipeId: number,
    name: String
}

export interface RatingCreateDto {
    recipeId: number,
    cost: number,
    taste: number,
    easeOfPrep: number,
    review: String
}
