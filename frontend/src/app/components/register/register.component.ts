import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {ToastrService} from "ngx-toastr";
import {userDto} from "../../dtos/user";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  error = false;
  errorMessage = '';

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private notification: ToastrService,
              private router: Router,
              private userService: UserService) {
    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      repeatPassword: ['']
    }, { validator: this.passwordMatchValidator });
  }

  ngOnInit() {}

  passwordMatchValidator(form: FormGroup) {
    return form.get('password').value === form.get('repeatPassword').value ? null : { passwordMismatch: true };
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  registerNewUser() {
    if (this.registerForm.valid) {
      const newUserRequest = {
        username: this.registerForm.controls.username.value,
        email: this.registerForm.controls.email.value,
        password: this.registerForm.controls.password.value
      };

      this.authService.registerUser(newUserRequest).subscribe({
        next: () => {
          console.log(`Successfully registered user: ${newUserRequest.email}`);
          this.setUserName();
          this.notification.success("Erfolgreich registriert als User:" + newUserRequest.email, "Registrierung erfolgreich!");
          this.router.navigate(['/']);  // Redirect after successful registration
        },
        error: (errorResponse) => {
          console.error('Registration error:', errorResponse);
          this.error = true;

          const errorMessage = errorResponse.error;
          const validationErrors = this.parseValidationErrors(errorMessage);
          validationErrors.forEach(error => {
            this.notification.error(error, "Registrierung Fehler");
          });
        }
      });
    } else {
      console.error('Invalid input');
      this.error = true;
      this.errorMessage = 'Bitte überprüfen Sie Ihre Eingabe und versuchen Sie es erneut!';
      this.notification.error(this.errorMessage, "Registrierung Fehler");
    }
  }

  parseValidationErrors(errorMessage) {
    let validationErrors = [];

    const type1Pattern = /Validation errors=\[(.*?)\]/;
    const type1Match = type1Pattern.exec(errorMessage);
    if (type1Match && type1Match[1]) {
      validationErrors = type1Match[1].split(', ').map(error => error.split(' ').slice(1).join(' '));
    } else {
      try {
        const errorObj = JSON.parse(errorMessage);
        if (errorObj.detail && errorObj.detail.includes('Fehlgeschlagene Validierung:')) {
          validationErrors = errorObj.detail.replace('Die Validierung der Eingabefelder ist fehlgeschlagen. Fehlgeschlagene Validierung: ', '').split(', ');
        } else {
          validationErrors.push("Ein unerwarteter Fehler ist aufgetreten.");
        }
      } catch (e) {
        validationErrors.push("Ein unerwarteter Fehler ist aufgetreten.");
      }
    }

    return validationErrors;
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

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

}
