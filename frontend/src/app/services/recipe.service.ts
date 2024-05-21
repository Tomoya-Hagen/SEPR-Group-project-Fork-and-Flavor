import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Recipe, RecipeDetailDto, RecipeListDto} from '../dtos/recipe';
import {SimpleRecipe} from "../dtos/SimpleRecipe";
import {DetailedRecipeDto} from "../dtos/DetailedRecipeDto";

const baseUri = environment.backendUrl + '/recipes';
@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  constructor(
    private http: HttpClient) {
  }

  public getListByPageAndStep(page:number, step: number): Observable<RecipeListDto[]> {
    return this.http.get<RecipeListDto[]>(
      baseUri+"/?page="+page+"&step="+step
    );
  }


  public getRecipeDetailsBy(recipeId: number): Observable<RecipeDetailDto> {
    return this.http.get<RecipeDetailDto>(
      baseUri+"/details/"+recipeId
    );
  }

  public recipeByName(name: string, limit: number | undefined): Observable<SimpleRecipe[]> {
    let params = new HttpParams();
    params = params.append("name", name);
    if (limit != null) {
      params = params.append("limit", limit);
    }
    return this.http.get<SimpleRecipe[]>(baseUri + "/simple", { params });
  }

  public createRecipe(recipe: Recipe): Observable<DetailedRecipeDto> {
    return this.http.post<DetailedRecipeDto>(baseUri, recipe);
  }
}
