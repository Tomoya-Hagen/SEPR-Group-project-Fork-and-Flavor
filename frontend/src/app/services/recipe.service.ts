import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {Recipe, RecipeDetailDto, RecipeListDto, RecipeUpdateDto} from '../dtos/recipe';
import {SimpleRecipe} from "../dtos/SimpleRecipe";
import {DetailedRecipeDto} from "../dtos/DetailedRecipeDto";
import { RecipeStepDescriptionDetailDto, RecipeStepDetailDto, RecipeStepRecipeDetailDto } from '../dtos/recipe-step';
import { Step } from '../dtos/Step';
import { map as rxjsMap} from 'rxjs/operators'
import { Observable, catchError, throwError } from 'rxjs';
import {Globals} from '../global/globals';
import {RecipeSearch} from "../dtos/recipe";
import { Page } from '../models/page.model';

/**
 * Service for handling recipe books.
 * It operates on the REST API.
 */
@Injectable({
  providedIn: 'root'
})

export class RecipeService {
  private baseUri = this.globals.backendUri + '/recipes';
  constructor(
    private http: HttpClient, private globals: Globals) {
  }

  /**
   * Get the recipes that match the given search parameter.
   * Parameters that are {@code null} are ignored.
   * The name is considered a match, if the given parameter is a substring of the field in recipe.
   *
   * @param searchParams the parameters to use in searching.
   * @param page the page number to get.
   * @param size the number of elements per page.
   * @return an Observable for the recipes where all given parameters match.
   */
  public search(searchParams: RecipeSearch, page: number, size: number): Observable<any> {
    const params = {
      name: searchParams.name,
      page: page.toString(),  // Ensure zero-based page indexing
      size: size.toString()
    };
    return this.http.get<any>(this.baseUri + '', { params });
  }

  recipesByName(name: string, limit: number): Observable<RecipeListDto[]> {
    let params = new HttpParams()
      .set('name', name)
      .set('limit', limit.toString());

    return this.http.get<Page<RecipeListDto>>(this.baseUri, { params })
      .pipe(
        rxjsMap((page: Page<RecipeListDto>) => page.content),  // Extract content from page object
        catchError((error) => {
          console.error(error);
          return throwError(() => new Error(error.message));
        })
      );
  }

  getGoesWellWith(id: number, page: number, size: number): Observable<Page<Recipe>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<Recipe>>(this.baseUri + '/' + id + '/goesWellWith', { params });
  }

  updateGoWellWithRecipes(id: number, recipes: Recipe[]): Observable<Recipe[]> {
    return this.http.put<Recipe[]>(this.baseUri + '/' + id + '/goesWellWith', recipes);
  }

  getRecipes(name: string, page: number, size: number): Observable<Page<Recipe>> {
    let params = new HttpParams()
      .set('name', name)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<Recipe>>(this.baseUri, { params });
  }


  public getRecipeDetailsBy(recipeId: number): Observable<RecipeDetailDto> {
    return this.http.get<RecipeDetailDto>(
      this.baseUri + "/details/" + recipeId
    );
  }

  public getEditRecipeDetailsBy(recipeId: number): Observable<RecipeDetailDto> {
    return this.http.get<RecipeDetailDto>(
      this.baseUri + "/edit/" + recipeId
    );
  }

  public recipeByName(name: string, limit: number | undefined): Observable<SimpleRecipe[]> {
    let params = new HttpParams();
    params = params.append("name", name);
    if (limit != null) {
      params = params.append("limit", limit);
    }
    return this.http.get<SimpleRecipe[]>(this.baseUri + "/simple", { params });
  }

  public createRecipe(recipe: Recipe): Observable<DetailedRecipeDto> {
    return this.http.post<DetailedRecipeDto>(this.baseUri, recipe);
  }

  public updateRecipe(recipe: RecipeUpdateDto): Observable<DetailedRecipeDto> {
      return this.http.put<DetailedRecipeDto>(this.baseUri + '/' + recipe.id, recipe);
  }

  public getRecipeUpdateDtoById(recipeId: number): Observable<RecipeUpdateDto> {
    return this.getRecipeDetailsBy(recipeId).pipe(
      rxjsMap(existingRecipe => this.mapToUpdateDto(existingRecipe)),
      catchError(error => {
        console.error('Error fetching recipe details:', error);
        return throwError(() => new Error('Failed to fetch recipe details: ' + error.message));
      })
    );
  }

  public getRecipeEditDtoById(recipeId: number): Observable<any> {
    return this.getEditRecipeDetailsBy(recipeId).pipe(
      rxjsMap(existingRecipe => this.mapToUpdateDto(existingRecipe)),
      catchError(error => {
        console.error('Error fetching recipe details:', error);
        return throwError(() => new Error('Failed to fetch recipe details: ' + error.message));
      })
    );
  }

  private mapToUpdateDto(existingRecipe: RecipeDetailDto): RecipeUpdateDto {
    return {
      id: existingRecipe.id,
      name: existingRecipe.name,
      description: existingRecipe.description,
      numberOfServings: existingRecipe.numberOfServings,
      categories: existingRecipe.categories.map(c => ({ id: c.id, name: c.name })),
      recipeSteps: this.mapSteps(existingRecipe.recipeSteps),
      ingredients: existingRecipe.ingredients
    };
  }
  private mapSteps(recipeSteps: RecipeStepDetailDto[]): Step[] {
    return recipeSteps.map(step => {
      if (step.hasOwnProperty('recipe')) {
        return {
          id: step.id,
          name: step.name,
          recipeId: (step as RecipeStepRecipeDetailDto).recipe.id,
          whichstep: false
        };
      } else {
        return {
          id: step.id,
          name: step.name,
          description: (step as RecipeStepDescriptionDetailDto).description,
          whichstep: true
        };
      }
    });
  }
}
