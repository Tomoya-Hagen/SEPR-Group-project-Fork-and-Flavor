import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/auth-request';
import {catchError, Observable, of} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {jwtDecode} from 'jwt-decode';
import {Globals} from '../global/globals';
import {NewUserRequest} from "../dtos/new-user-request";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';
  private currentUserId: number | null = null;

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }


  /**
   * Is the user logged in

   *
   */
  isLogged(): Observable<boolean> {
    return this.httpClient.get<boolean>(this.authBaseUri + "/islogged");
  }


  /**
   * Register a new user. If it was successful, a valid JWT token will be stored
   *
   * @param newUserRequest
   */
  registerUser(newUserRequest: NewUserRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri + '/register', newUserRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }


  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return !!this.getToken() && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
    this.currentUserId = null;
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwtDecode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  getCurrentUserId(): number | null {
    if (this.currentUserId === null && this.getToken() != null) {
      this.decodeAndStoreUserId(this.getToken());
    }
    return this.currentUserId;
  }

  private decodeAndStoreUserId(token: string) {
    const decoded: any = jwtDecode(this.getToken());
    this.currentUserId = decoded.userId;
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date {

    const decoded: any = jwtDecode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

}
