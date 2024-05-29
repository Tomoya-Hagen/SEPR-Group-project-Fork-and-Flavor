import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError } from "rxjs";
import { environment } from "src/environments/environment";
import {Globals} from '../global/globals';
import { userListDto } from "../dtos/user";

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

    usersByName(name: string, limit: number): Observable<userListDto[]> {
        let params = new HttpParams();
        params = params.append('name', name);
        params = params.append('limit', limit.toString());
        return this.http.get<userListDto[]>(this.baseUri, { params })
        .pipe(
            catchError((error) => {
                console.error(error);
                throw error;
            })
        );
    }

}
