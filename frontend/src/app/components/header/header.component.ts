import {Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent{

  constructor(public authService: AuthService) { }

  isCollapsed = false;
  getUser(): string {
    return localStorage.getItem("username");
  }

  getUserId(): number {
    return Number(localStorage.getItem("userId"));
  }
}
