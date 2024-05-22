import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError } from "rxjs";
import { environment } from "src/environments/environment";
import { UserListDto } from "../dtos/user";

const baseUri = environment.backendUrl + '/users';

/**
 * Service for handling users.
 * It operates on the REST API.
 */
@Injectable({
    providedIn: 'root'
  })
export class UserService {

    constructor(
        private http: HttpClient,
    ) { }

    usersByName(name: string, limit: number): Observable<UserListDto[]> {
        let params = new HttpParams();
        params = params.append('name', name);
        params = params.append('limit', limit.toString());
        return this.http.get<UserListDto[]>(baseUri, { params })
        .pipe(
            catchError((error) => {
                console.error(error);
                throw error;
            })
        );
    }

}