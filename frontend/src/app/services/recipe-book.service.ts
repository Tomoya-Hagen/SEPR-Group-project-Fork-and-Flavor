import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RecipeBook, RecipeBookCreateDto } from '../dtos/recipe-book';
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environments/environment';

const baseUri = environment.backendUrl + '/recipebook';

/**
 * Service for handling recipe books.
 * It operates on the REST API.
 */
@Injectable({
    providedIn: 'root'
  })
export class RecipeBookService {

    constructor(
        private http: HttpClient,
    ) { }

    createRecipeBook(recipeBook: RecipeBookCreateDto): Observable<RecipeBook> {
        return this.http.post<RecipeBook>(baseUri, recipeBook)
        .pipe(
            catchError((error) => {
                console.error(error);
                throw error;
            })
        );
    }

    getById(id: number): Observable<RecipeBook> {
        return this.http.get<RecipeBook>(baseUri + '/' + id)
        .pipe(
            catchError((error) => {
                console.error(error.error);
                throw error;
            })
        );
    }

}