import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {UserService} from "../../services/user.service";
import {userDto} from "../../dtos/user";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent implements OnInit {

  protected user: userDto;

  constructor(public authService: AuthService, private userService: UserService, private notification: ToastrService) { }

  ngOnInit() {
    this.userService.getCurrentUser().subscribe({
      next: (data: any) => {
        this.user = data;
      },
      error: (error: any) => {
        console.error('Error fetching recipes', error);
        this.notification.error('Could not fetch recipes', 'Error');
      }
    })
  }
}
