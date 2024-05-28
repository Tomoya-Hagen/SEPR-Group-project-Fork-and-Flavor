import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {Router} from "@angular/router";
import {NewUserRequest} from "../../dtos/new-user-request";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  error = false;
  errorMessage = '';
  submitted = false;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
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
          this.router.navigate(['/']);  // Redirect after successful registration
        },
        error: (error) => {
          console.error('Registration error:', error);
          this.error = true;
          // Adjust below line based on your actual error response format
          this.errorMessage = error.error ? error.error : 'Unknown error occurred. Please try again.';
        }
      });
    } else {
      console.error('Invalid input');
      this.error = true;
      this.errorMessage = 'Please check your input and try again.';
    }
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  vanishError() {
    this.error = false;
    this.errorMessage = '';
  }

}
