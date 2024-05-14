import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Ingredient} from "../dtos/Ingredient";
import {SimpleRecipe} from "../dtos/SimpleRecipe";
import {SimpleCategory} from "../dtos/SimpleCategory";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private recipeBaseUri: string = this.globals.backendUri + '/category';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  public categoryByName(name: string, limit: number | undefined): Observable<SimpleCategory[]> {
    let params = new HttpParams();
    params = params.append("name", name);
    if (limit != null) {
      params = params.append("limit", limit);
    }
    return this.httpClient.get<SimpleCategory[]>(this.recipeBaseUri, { params });
  }

}
