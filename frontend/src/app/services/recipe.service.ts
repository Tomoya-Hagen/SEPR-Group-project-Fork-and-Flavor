import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Recipe } from '../dtos/recipe';
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environments/environment';

const baseUri = environment.backendUrl + '/recipes';

/**
 * Service for handling recipe books.
 * It operates on the REST API.
 */
@Injectable({
    providedIn: 'root'
  })
export class RecipeService {

    constructor(
        private http: HttpClient,
    ) { }


    recipesByName(name: string, limit: number): Observable<Recipe[]> {
        let params = new HttpParams();
        params = params.append('name', name);
        params = params.append('limit', limit.toString());
        return this.http.get<Recipe[]>(baseUri, { params })
        .pipe(
            catchError((error) => {
                console.error(error);
                throw error;
            })
        );
    }

}