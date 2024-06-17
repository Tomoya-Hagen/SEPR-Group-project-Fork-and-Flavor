import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {

  resetPasswordForm: UntypedFormGroup;
  submitted = false;
  error = false;
  errorMessage = '';

  constructor(private formBuilder: UntypedFormBuilder,
              private notification: ToastrService,
              private authService: AuthService,
              private router: Router) {
    this.resetPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  /**
   * Form validation will start after the method is called and a password reset request will be sent
   */
  resetPassword() {
    this.submitted = true;
    if (this.resetPasswordForm.valid) {
      const email = this.resetPasswordForm.controls.email.value;
      this.authService.resetPassword(email).subscribe({
        next: () => {
          this.notification.success('Ein neues Passwort wurde an die Email-Adresse gesendet: ' + email, 'Passwort zurücksetzen erfolgreich!');
          this.router.navigate(['/login']);
        },
        error: error => {
          console.log('Could not send reset link due to:');
          console.log(error);
          this.error = true;
          if (error.status === 404) {
            this.notification.error(this.errorMessage,'Email wurde nicht gefunden!');
          }
        }
      });
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  ngOnInit() {
  }

  goToRegister(){
    this.router.navigate(['/register']);
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

}
