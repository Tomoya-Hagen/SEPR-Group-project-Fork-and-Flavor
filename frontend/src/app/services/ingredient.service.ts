import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {IngredientDetailDto} from "../dtos/ingredient";

@Injectable({
  providedIn: 'root'
})
export class IngredientService {

  private ingredientBaseUri: string = this.globals.backendUri + '/ingredients';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  public ingredientssByName(name: string, limit: number | undefined): Observable<IngredientDetailDto[]> {
    let params = new HttpParams();
    params = params.append("name", name);
    if (limit != null) {
      params = params.append("limit", limit);
    }
    return this.httpClient.get<IngredientDetailDto[]>(this.ingredientBaseUri, { params });
  }

  public allingredients(): Observable<IngredientDetailDto[]> {
    return this.httpClient.get<IngredientDetailDto[]>(this.ingredientBaseUri + "/all");
  }

  public allunits(): Observable<string[]> {
    return this.httpClient.get<string[]>(this.ingredientBaseUri + "/units");
  }
}
