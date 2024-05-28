import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { RecipeDetailDto, RecipeListDto } from '../dtos/recipe';
import {RecipeList, RecipeSearch} from "../dtos/recipe";

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
  ) {
  }

  /**
   * Get the recipes that match the given search parameter.
   * Parameters that are {@code null} are ignored.
   * The name is considered a match, if the given parameter is a substring of the field in recipe.
   *
   * @param searchParameters the parameters to use in searching.
   * @return an Observable for the recipes where all given parameters match.
   */
  search(searchParams: RecipeSearch): Observable<RecipeList[]> {
    if (searchParams.name) {
      delete searchParams.name;
    }
    let params = new HttpParams();
    if (searchParams.name) {
      params = params.append('name', searchParams.name);
    }
    return this.http.get<RecipeList[]>(baseUri + "/search", {params});
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
}
