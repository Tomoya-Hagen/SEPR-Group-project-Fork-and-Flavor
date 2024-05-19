import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Recipe } from '../dtos/recipe';
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environments/environment';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { RecipeDetailDto, RecipeListDto } from '../dtos/recipe';

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
        private http: HttpClient,
    ) { }


    recipesByName(name: string, limit: number): Observable<Recipe[]> {
        let params = new HttpParams();
        params = params.append('name', name);
        params = params.append('limit', limit.toString());
        return this.http.get<Recipe[]>(baseUri, { params })
        .pipe(
            catchError((error) => {
                console.error(error);
                throw error;
            })
        );
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
}