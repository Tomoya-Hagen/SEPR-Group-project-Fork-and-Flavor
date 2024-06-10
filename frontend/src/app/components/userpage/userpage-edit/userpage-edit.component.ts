import { Component } from '@angular/core';
import {NgIf} from "@angular/common";
import {ReactiveFormsModule, UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import { userPasswordChangeDto} from "../../../dtos/user";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-userpage-edit',
  standalone: true,
    imports: [
        NgIf,
        ReactiveFormsModule
    ],
  templateUrl: './userpage-edit.component.html',
  styleUrl: './userpage-edit.component.scss'
})
export class UserpageEditComponent {

  loginForm: UntypedFormGroup;
  bannerError: string | null = null;
  // After first submission attempt, form validation will start
  submitted = false;

  constructor(
    private service: UserService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      oldPassword: ['', [Validators.required]],
      newPassword1: ['', [Validators.required, Validators.minLength(8)]],
      newPassword2: ['', [Validators.required]]
    });
  }

  editPassword() {
    this.submitted = true;
    if (this.loginForm.controls.newPassword1.value === this.loginForm.controls.newPassword2.value){
      if (this.loginForm.valid) {
        let passwordChange: userPasswordChangeDto = {
          newPassword: this.loginForm.controls.newPassword1.value,
          oldPassword: this.loginForm.controls.oldPassword.value
        };
        this.route.params.subscribe(params => {
          let observable = this.service.updatePassword(params['id'], passwordChange);
          observable.subscribe({
            next: data => {
              this.authService.logoutUser()
              this.router.navigate(['/login']);
            },
            error: error => {
              console.error('Error changing password for user with id', error);
              this.bannerError = error.message;
              const errorMessage = error.status === 0
                ? 'Is the backend up?'
                : error.message.message;
              this.notification.error(errorMessage, 'Error changing password for user with id');
            }
          });
        });
      } else {
        console.log('Invalid input');
      }
    } else {
      console.log('new passwords doesnt match');
      this.notification.error( 'new passwords doesnt match');
    }
  }


}
