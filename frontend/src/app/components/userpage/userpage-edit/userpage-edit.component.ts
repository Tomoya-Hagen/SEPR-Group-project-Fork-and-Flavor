import { Component } from '@angular/core';
import {NgIf} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";

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

  editpassword() {

  }
}
