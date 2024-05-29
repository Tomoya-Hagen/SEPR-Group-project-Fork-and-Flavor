import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RecipeBook, RecipeBookCreateDto } from '../dtos/recipe-book';
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environments/environment';
import {Globals} from '../global/globals';


/**
 * Service for handling recipe books.
 * It operates on the REST API.
 */
@Injectable({
    providedIn: 'root'
  })
export class RecipeBookService {
    private baseUri = this.globals.backendUri + '/recipebook';
    constructor(
        private http: HttpClient, private globals: Globals
    ) { }

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
        return this.http.get<RecipeBook>(this.baseUri + '/' + id)
        .pipe(
            catchError((error) => {
                console.error(error.error);
                throw error;
            })
        );
    }

}