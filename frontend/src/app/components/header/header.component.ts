import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent implements OnInit{

  user: String = "";
  id: number = 0;

  constructor(public authService: AuthService) { }

  ngOnInit(): void {
    this.user = localStorage.getItem("username");
    this.id = Number(localStorage.getItem("userId"));
    }
}
