import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {Recipe, RecipeDetailDto, RecipeListDto} from '../dtos/recipe';
import {SimpleRecipe} from "../dtos/SimpleRecipe";
import {DetailedRecipeDto} from "../dtos/DetailedRecipeDto";
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { RecipeDetailDto, RecipeListDto } from '../dtos/recipe';
import {RecipeList, RecipeSearch} from "../dtos/recipe";
import {RecipeBookListDto, RecipeBookSearch} from "../dtos/recipe-book";

const baseUri = environment.backendUrl + '/recipes';

/**
 * Service for handling recipe books.
 * It operates on the REST API.
 */
@Injectable({
  providedIn: 'root'
})

export class RecipeService {

  constructor(
    private http: HttpClient) {
  }

  constructor(
    private http: HttpClient,
  ) {
  }

  /**
   * Get the recipes that match the given search parameter.
   * Parameters that are {@code null} are ignored.
   * The name is considered a match, if the given parameter is a substring of the field in recipe.
   *
   * @param searchParams the parameters to use in searching.
   * @return an Observable for the recipes where all given parameters match.
   */
  public search(searchParams: RecipeSearch): Observable<RecipeListDto[]> {
    return this.http.get<RecipeListDto[]>(baseUri+"/search?name="+searchParams.name);

  }


  recipesByName(name: string, limit: number): Observable<RecipeListDto[]> {
    let params = new HttpParams();
    params = params.append('name', name);
    params = params.append('limit', limit.toString());
    return this.http.get<RecipeListDto[]>(baseUri, {params})
      .pipe(
        catchError((error) => {
          console.error(error);
          throw error;
        })
      );
  }

  public getListByPageAndStep(page: number, step: number): Observable<RecipeListDto[]> {
    return this.http.get<RecipeListDto[]>(
      baseUri + "/?page=" + page + "&step=" + step
    );
  }


  public getRecipeDetailsBy(recipeId: number): Observable<RecipeDetailDto> {
    return this.http.get<RecipeDetailDto>(
      baseUri + "/details/" + recipeId
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
