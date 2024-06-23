import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Globals } from '../global/globals';
import { RatingCreateDto, RatingListDto } from '../dtos/rating';
import { Observable } from 'rxjs/internal/Observable';
import {userDto} from "../dtos/user";

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  private baseUri = this.globals.backendUri + '/ratings';
  constructor(
    private http: HttpClient, private globals: Globals) {
  }

  public createRating(rating: RatingCreateDto): Observable<RatingListDto> {
    return this.http.post<RatingListDto>(this.baseUri, rating);
  }

  public getRatingsByRecipeId(recipeId: number): Observable<RatingListDto[]> {
    return this.http.get<RatingListDto[]>(
      this.baseUri + "/recipe/" + recipeId
    );
  }

  public getRatingsByUserId(userId: number): Observable<RatingListDto[]> {
    return this.http.get<RatingListDto[]>(this.baseUri + "/user/" + userId);
  }
}
