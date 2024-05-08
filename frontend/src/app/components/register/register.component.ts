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
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  registerNewUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      const newUserRequest: NewUserRequest = new NewUserRequest(this.registerForm.controls.username.value, this.registerForm.controls.email.value, this.registerForm.controls.password.value);
      this.authService.registerUser(newUserRequest).subscribe({
        next: () => {
          console.log('Successfully registered user: ' + newUserRequest.email);
          this.router.navigate(['/message']);
        },
        error: error => {
          console.log('Could not register due to:');
          console.log(error);
          this.error = true;
          if (typeof error.error === 'object') {
            this.errorMessage = error.error.error;
          } else {
            this.errorMessage = error.error;
          }
        }
      })
    } else {
      console.log('Invalid input');
    }
  }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  onSubmit(): void {
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

}
