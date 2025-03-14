import {Component, OnInit} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {AuthRequest} from '../../dtos/auth-request';
import {ToastrService} from "ngx-toastr";
import {userDto} from "../../dtos/user";
import {UserService} from "../../services/user.service";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: UntypedFormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(private formBuilder: UntypedFormBuilder,
              private notification: ToastrService,
              private authService: AuthService, private router: Router,
              private userService: UserService) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  loginUser() {
    this.submitted = true;
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(this.loginForm.controls.username.value, this.loginForm.controls.password.value);
      this.authenticateUser(authRequest);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the message page
   *
   * @param authRequest authentication data from the user login form
   */
  authenticateUser(authRequest: AuthRequest) {
    console.log('Trying to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        this.setUserName();
        console.log('Successfully logged in user: ' + authRequest.email.toString());
        this.notification.success('Erfolgreich eingeloggt als Benutzer: ' + authRequest.email.toString(), "Authentifizierung erfolgreich!")
        this.router.navigate(['/']);
      },
      error: error => {
        console.log('Could not log in due to:');
        console.log(error);
        this.notification.error("E-Mail oder Passwort falsch.")
      }
    });
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  ngOnInit() {
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }

  goToForgotPassword() {
    this.router.navigate(['/login/reset']);
  }

  setUserName() {
    this.userService.getCurrentUser().subscribe({
      next: (data: userDto) => {
        localStorage.setItem("username",data.name);
        localStorage.setItem("userId",String(data.id));
      },
      error: (error: any) => {
        this.notification.error('You are not logged in as user', 'Authentication Error');
      }
    })
  }
}
