import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {SimpleRecipe} from "../dtos/SimpleRecipe";
import {Recipe} from "../dtos/Recipe";
import {DetailedRecipeDto} from "../dtos/DetailedRecipeDto";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  private recipeBaseUri: string = this.globals.backendUri + '/recipe';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  public recipeByName(name: string, limit: number | undefined): Observable<SimpleRecipe[]> {
    let params = new HttpParams();
    params = params.append("name", name);
    if (limit != null) {
      params = params.append("limit", limit);
    }
    return this.httpClient.get<SimpleRecipe[]>(this.recipeBaseUri, { params });
  }

  public createRecipe(recipe: Recipe): Observable<DetailedRecipeDto> {
    return this.httpClient.post<DetailedRecipeDto>(this.recipeBaseUri, recipe);
  }

}
