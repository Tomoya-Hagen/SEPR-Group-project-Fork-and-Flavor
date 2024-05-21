import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { RecipeDetailDto } from '../dtos/recipe';
import {RecipeBookListDto, RecipeBookSearch} from "../dtos/recipe-book";

const baseUri = environment.backendUrl + '/recipebook';
@Injectable({
  providedIn: 'root'
})
export class RecipeBookService {

  constructor(
    private http: HttpClient) {
  }

  public getAllRecipeBooks(): Observable<RecipeBookListDto[]> {
    return this.http.get<RecipeBookListDto[]>(
      baseUri
    );
  }


  public getRecipeDetailsBy(recipeId: number): Observable<RecipeDetailDto> {
    return this.http.get<RecipeDetailDto>(
      baseUri+"/"+recipeId+"/details"
    );
  }

  public search(searchParams: RecipeBookSearch): Observable<RecipeBookListDto[]> {
    if (searchParams.name) {
      delete searchParams.name;
    }
    let params = new HttpParams();
    if (searchParams.name) {
      params = params.append('name', searchParams.name);
    }
    return this.http.get<RecipeBookListDto[]>(baseUri+"/search", {params});
  }

}
