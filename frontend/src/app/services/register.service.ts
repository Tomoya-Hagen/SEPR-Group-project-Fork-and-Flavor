import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {tap} from "rxjs/operators";
import {NewUserRequest} from "../dtos/new-user-request";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private authBaseUri: string = this.globals.backendUri + '/register';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Register the user. If it was successful, a valid JWT token will be stored
   *
   * @param newUserRequest User data
   */
  registerUser(newUserRequest: NewUserRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, newUserRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }
}
