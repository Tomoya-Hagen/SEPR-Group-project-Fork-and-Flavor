import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, mergeMap, throwError} from 'rxjs';
import {Recipe, RecipeDetailDto, RecipeListDto, RecipeUpdateDto} from '../dtos/recipe';
import {SimpleRecipe} from "../dtos/SimpleRecipe";
import {DetailedRecipeDto} from "../dtos/DetailedRecipeDto";
import { RecipeStepDescriptionDetailDto, RecipeStepDetailDto, RecipeStepRecipeDetailDto } from '../dtos/recipe-step';
import { Step } from '../dtos/Step';
import { map as rxjsMap, catchError } from 'rxjs/operators';

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
