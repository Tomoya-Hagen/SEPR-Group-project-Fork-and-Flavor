import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { RecipeDetailDto } from '../dtos/recipe';
import {RecipeBookListDto} from "../dtos/recipe-book";

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
      baseUri+"/details/"+recipeId
    );
  }
}
