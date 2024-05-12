import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { RecipeListDto } from '../dtos/recipe';

const baseUri = environment.backendUrl + '/recipes';
@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  constructor( 
    private http: HttpClient) {
  }

  public getListByPageAndStep(page:number, step: number): Observable<RecipeListDto[]> {
    return this.http.get<RecipeListDto[]>(
      baseUri+"/?page="+page+"&step="+step
    );
  }
}
