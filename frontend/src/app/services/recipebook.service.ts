import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RecipeBookDetailDto, RecipeBookListDto, RecipeBookSearch} from "../dtos/recipe-book";

const baseUri = environment.backendUrl + '/recipebook';
@Injectable({
  providedIn: 'root'
})
export class RecipeBookService {
  spoonRecipe(recipeId: number, recipeBookId: number): Observable<RecipeBookDetailDto> {
    return this.http.patch<RecipeBookDetailDto>(baseUri+"/"+recipeBookId+"/spoon/"+recipeId,null);
  }
  getRecipeBooksTheUserHasWriteAccessTo(): Observable<RecipeBookListDto[]> {
    return this.http.get<RecipeBookListDto[]>(baseUri+"/user");
  }

  constructor(
    private http: HttpClient) {
  }

  public getAllRecipeBooksBySteps(page: number, step: number): Observable<RecipeBookListDto[]> {
    return this.http.get<RecipeBookListDto[]>(
      baseUri+"/?page="+page+"&step="+step
    );
  }


  public getRecipeBookDetailsBy(recipeId: number): Observable<RecipeBookDetailDto> {
    return this.http.get<RecipeBookDetailDto>(
      baseUri+"/"+recipeId+"/details"
    );
  }

  public search(searchParams: RecipeBookSearch): Observable<RecipeBookListDto[]> {
        return this.http.get<RecipeBookListDto[]>(baseUri+"/search?name="+searchParams.name);

  }

}
