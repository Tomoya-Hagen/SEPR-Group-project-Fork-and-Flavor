import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {RecipeList, RecipeSearch} from "../dtos/recipe";

const baseUri = environment.backendUrl + '/recipes';

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
    return this.http.get<RecipeList[]>(baseUri+"/search", {params});
  }

}
