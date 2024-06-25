import {AfterViewInit, Component, ElementRef, HostListener, NgZone, OnInit, ViewChild} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute, Route, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {tap} from "rxjs/operators";
import {catchError, of} from "rxjs";
import {NgForOf, NgIf} from "@angular/common";
import {WeekplanService} from "../../services/weekplan.service";

@Component({
  selector: 'app-weekplan',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './weekplan.component.html',
  styleUrl: './weekplan.component.scss'
})
export class WeekplanComponent implements OnInit{

  constructor(
    private notification: ToastrService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private weekplanService: WeekplanService,
    private ngZone: NgZone
  ) { }
  mobile = false;

  offset = 0;
  more = true;

  @ViewChild('scrollContainer') scrollContainer!: ElementRef;

  weekplan:any[]
  id: number

  ngOnInit(): void {

    if (window.screen.width <= 768) { // 768px portrait
      this.mobile = true;
    }

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

    this.generateDates();
  }


  onScroll(event: any) {
    const element = event.target;
    if (element.scrollLeft + element.clientWidth >= element.scrollWidth - 10) {
      this.generateDates(); // Load new dates when scrolled to the end
    }
  }

  generateDates() {
    if(this.more){
      let start = new Date()
      let end = new Date()
      start.setDate(start.getDate() + this.offset);
      end.setDate(end.getDate() + 7 + this.offset);
      this.offset+=8;
      this.id = this.route.snapshot.params["id"]
      this.weekplanService.getWeekplanDetail(parseInt(this.route.snapshot.params["id"]),start,end ).subscribe(weekplan => {
        if(this.weekplan){
          if(weekplan.length === 0){
            this.more = false;
            return;
          }
          this.weekplan.push(...this.convertWeekplanDates(weekplan));
        } else {
          this.weekplan = this.convertWeekplanDates(weekplan);
          if(this.weekplan.length < 7){
            this.weekplanService.getWeekplanExtendDetail(parseInt(this.route.snapshot.params["id"]),end,7 ).subscribe(weekplan => {
              this.weekplan.push(...this.convertWeekplanDates(weekplan));
            })
          }
        }
      })
    }
  }

  convertWeekplanDates(weekplan: any[]): any[] {
    return weekplan.map(item => {
      const dateObj = new Date(item.date);
      return{
      ...item,
      date: dateObj,
      day: dateObj.toLocaleString('default', { weekday: 'long' })
      };
    });
  }


  redirect(id: number){
    this.router.navigate(["/recipe/details/"+id]);
  }

  goToCreateWeekplan() {
    this.router.navigate(['/weekplan/' + this.id + '/create']);
  }

  protected readonly event = event;
}
