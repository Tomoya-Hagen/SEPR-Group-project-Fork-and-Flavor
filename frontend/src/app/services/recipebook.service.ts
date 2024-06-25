import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {
  RecipeBook,
  RecipeBookCreateDto,
  RecipeBookDetailDto,
  RecipeBookListDto,
} from "../dtos/recipe-book";

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


  public getRecipeBookDetailsBy(recipeId: number): Observable<RecipeBookDetailDto> {
    return this.http.get<RecipeBookDetailDto>(
      this.baseUri+"/"+recipeId+"/details"
    );
  }

  /**
   * Get the recipes that match the given search parameter.
   * Parameters that are {@code null} are ignored.
   * The name is considered a match, if the given parameter is a substring of the field in recipe.
   *
   * @param name the parameters to use in searching.
   * @param page the page number to get.
   * @param size the number of elements per page.
   * @return an Observable for the recipes where all given parameters match.
   */
  public search(name: string, page: number, size: number): Observable<any> {
    const params = {
      name: name,
      page: page.toString(),  // Ensure zero-based page indexing
      size: size.toString()
    };
    return this.http.get<any>(this.baseUri + '', { params });
  }

  public getBestRecipeBooks(size: number): Observable<any> {
    const params = {  // Ensure zero-based page indexing
      size: size.toString()
    };
    return this.http.get<any>(this.baseUri + '/best', { params });
  }

  createRecipeBook(recipeBook: RecipeBookCreateDto): Observable<RecipeBook> {
    return this.http.post<RecipeBook>(this.baseUri, recipeBook)
      .pipe(
        catchError((error) => {
          console.error(error);
          throw error;
        })
      );
  }

  getById(id: number): Observable<RecipeBook> {
    return this.http.get<RecipeBook>(this.baseUri + '/' + id + '/details')
      .pipe(
        catchError((error) => {
          console.error(error.error);
          throw error;
        })
      );
  }


  update(recipeBook: RecipeBookCreateDto, id: number) {
     return this.http.patch(this.baseUri + '/' + id + '/update', recipeBook);
  }

  getUserIdByRecipeBookId(id: number) {
    return this.http.get<number>(this.baseUri + "/" + id + "/getUserId");
  }
}
