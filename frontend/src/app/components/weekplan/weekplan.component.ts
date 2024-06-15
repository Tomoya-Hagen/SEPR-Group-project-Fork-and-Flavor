import {Component, OnInit} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {tap} from "rxjs/operators";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-weekplan',
  standalone: true,
  imports: [],
  templateUrl: './weekplan.component.html',
  styleUrl: './weekplan.component.scss'
})
export class WeekplanComponent implements OnInit{

  constructor(
    private notification: ToastrService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.isLogged()
      .pipe(
        tap((isLoggedIn: boolean) => {
          console.log('Is logged in:', isLoggedIn);
        }),
        catchError((error) => {
          console.error('Error:', error);
          this.notification.error('Sie müssen sich als Benutzer anmelden oder als Benutzer registrieren, um ein Wochenplan zu sehen.' , 'Wochenplan kann nicht geladen werden.');
          this.router.navigate(['/login']);
          return of(false); // Handle the error and return a fallback value
        })
      )
      .subscribe();
  }

}
