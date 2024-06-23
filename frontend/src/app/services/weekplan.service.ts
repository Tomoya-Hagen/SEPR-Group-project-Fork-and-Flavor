import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {Recipe, RecipeDetailDto, RecipeListDto, RecipeUpdateDto} from '../dtos/recipe';
import {SimpleRecipe} from "../dtos/SimpleRecipe";
import {DetailedRecipeDto} from "../dtos/DetailedRecipeDto";
import { RecipeStepDescriptionDetailDto, RecipeStepDetailDto, RecipeStepRecipeDetailDto } from '../dtos/recipe-step';
import { Step } from '../dtos/Step';
import { map as rxjsMap} from 'rxjs/operators'
import { Observable, catchError, throwError } from 'rxjs';
import {Globals} from '../global/globals';
import {RecipeSearch} from "../dtos/recipe";
import { Page } from '../models/page.model';
import { WeekPlanCreateDto } from '../dtos/weekplan';

/**
 * Service for handling recipe books.
 * It operates on the REST API.
 */
@Injectable({
  providedIn: 'root'
})

export class WeekplanService {
  private baseUri = this.globals.backendUri + '/weekplaner';
  constructor(
    private http: HttpClient, private globals: Globals) {
  }

  createWeekplan(createDto: WeekPlanCreateDto) : Observable<any> {
   return this.http.post<any>(this.baseUri,createDto); 
  }

  getWeekplanDetail(id: number,start: Date, end: Date): Observable<any> {
    let params = new HttpParams()
      .set('from', start.toISOString().split('T')[0])
      .set('to', end.toISOString().split('T')[0]);
    return this.http.get<any>(this.baseUri +  "/" +id,{params: params} );
  }

}
