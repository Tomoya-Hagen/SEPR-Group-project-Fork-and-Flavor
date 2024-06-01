import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {RecipeBookDetailDto, RecipeBookListDto, RecipeBookSearch} from "../dtos/recipe-book";

@Injectable({
  providedIn: 'root'
})
export class RecipeBookService {
  private baseUri = this.globals.backendUri + '/recipebook';
  spoonRecipe(recipeId: number, recipeBookId: number): Observable<RecipeBookDetailDto> {
    return this.http.patch<RecipeBookDetailDto>(this.baseUri+"/"+recipeBookId+"/spoon/"+recipeId,null);
  }
  getRecipeBooksTheUserHasWriteAccessTo(): Observable<RecipeBookListDto[]> {
    return this.http.get<RecipeBookListDto[]>(this.baseUri+"/user");
  }

  constructor(
    private http: HttpClient, private globals: Globals) {
  }

  public getAllRecipeBooksBySteps(page: number, step: number): Observable<RecipeBookListDto[]> {
    return this.http.get<RecipeBookListDto[]>(
      this.baseUri+"/?page="+page+"&step="+step
    );
  }


  public getRecipeBookDetailsBy(recipeId: number): Observable<RecipeBookDetailDto> {
    return this.http.get<RecipeBookDetailDto>(
      this.baseUri+"/"+recipeId+"/details"
    );
  }

  public search(searchParams: RecipeBookSearch): Observable<RecipeBookListDto[]> {
        return this.http.get<RecipeBookListDto[]>(this.baseUri+"/search?name="+searchParams.name);

  }

}
