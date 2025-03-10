import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, map } from "rxjs";
import {Globals} from '../global/globals';
import {userDto, userListDto, userPasswordChangeDto} from "../dtos/user";
import {RecipeBookListDto} from "../dtos/recipe-book";
import {RecipeListDto} from "../dtos/recipe";

/**
 * Service for handling users.
 * It operates on the REST API.
 */
@Injectable({
    providedIn: 'root'
  })
export class UserService {
    private baseUri = this.globals.backendUri + '/users';
    constructor(
        private http: HttpClient, private globals: Globals
    ) { }

    usersByName(name: string, limit: number, currentUserId: number): Observable<userListDto[]> {
        let params = new HttpParams();
        params = params.append('name', name);
        params = params.append('limit', limit.toString());
        return this.http.get<userListDto[]>(this.baseUri, { params })
        .pipe(
          map((users: userListDto[]) => users.filter(user => user.id !== currentUserId)),
            catchError((error) => {
                console.error(error);
                throw error;
            })
        );
    }

    public getUser(id: number): Observable<userDto> {
      return this.http.get<userDto>(this.baseUri+"/"+id+"/details");
    }

    public getAllRecipeBooksForUserId(id:number): Observable<RecipeBookListDto[]> {
      return this.http.get<RecipeBookListDto[]>(this.baseUri+"/"+id+"/recipebooks");
    }

  public getAllRecipesForUserId(id:number): Observable<RecipeListDto[]> {
    return this.http.get<RecipeListDto[]>(this.baseUri+"/"+id+"/recipes");
  }

  public getCurrentUser(): Observable<userDto> {
    return this.http.get<userDto>(`${this.baseUri}/current`)
      .pipe(
        catchError((error) => {
          console.error(error);
          throw error;
        })
      );
  }

  public getBadgesOfCurrentUser(): Observable<any> {
    return this.http.get<String>(this.baseUri+"/badge");
  }

  public getBadgesOfUser(id: number): Observable<any> {
    return this.http.get<String>(this.baseUri + "/" + id + "/badge");
  }

  public updatePassword(id: number, passwordChange: userPasswordChangeDto) {
    return this.http.patch(this.baseUri+"/changePassword/"+id,passwordChange);
  }
}
