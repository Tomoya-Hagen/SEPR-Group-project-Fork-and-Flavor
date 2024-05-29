import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable, mergeMap, throwError} from 'rxjs';
import {Recipe, RecipeDetailDto, RecipeListDto, RecipeUpdateDto} from '../dtos/recipe';
import {SimpleRecipe} from "../dtos/SimpleRecipe";
import {DetailedRecipeDto} from "../dtos/DetailedRecipeDto";
import { RecipeStepDescriptionDetailDto, RecipeStepDetailDto, RecipeStepRecipeDetailDto } from '../dtos/recipe-step';
import { Step } from '../dtos/Step';
import { map as rxjsMap, catchError } from 'rxjs/operators'
import { Observable, catchError } from 'rxjs';
import {RecipeSearch} from "../dtos/recipe";

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

  public getRecipeNameBy(recipeId: number): Observable<string> {
    return this.http.get<RecipeDetailDto>(
      baseUri+"/details/"+recipeId
    ).pipe(
      rxjsMap(recipe => recipe.name)
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

  public updateRecipe(recipe: RecipeUpdateDto): Observable<DetailedRecipeDto> {
      return this.http.put<DetailedRecipeDto>(baseUri + '/' + recipe.id, recipe);
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
          name: (step as RecipeStepRecipeDetailDto).recipe.name,
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
