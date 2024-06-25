import { Component, OnInit } from '@angular/core';
import { Form } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs/internal/Observable';
import { RecipeBookListDto } from 'src/app/dtos/recipe-book';
import { Daytime, WeekDayDto, WeekDayEntity, WeekPlanCreateDto, Weekday } from 'src/app/dtos/weekplan';
import { RecipeBookService } from 'src/app/services/recipebook.service';
import { WeekplanService } from 'src/app/services/weekplan.service';

@Component({
  selector: 'app-weekplan-create',
  templateUrl: './weekplan-create.component.html',
  styleUrl: './weekplan-create.component.scss'
})
export class WeekplanCreateComponent implements OnInit {
  public Daytime = Daytime;
  fromDate: string | null = null;
  toDate: string | null = null;
  todayDate: string;
  createDto: WeekPlanCreateDto = {
    startDate: null,
    endDate: null,
    weekdays: [],
    recipeBookId: 0
  };
  currentRecipeBook: RecipeBookListDto | null = null;
  dummyRecipeBookSelectionModel: unknown;
  recipeBookSuggestions = (input: string): Observable<RecipeBookListDto[]> =>
    this.recipeBookService.getRecipeBooksTheUserHasWriteAccessTo()

  weekdays : WeekDayEntity[]= [
    {
      weekday: Weekday.Monday,
      dayTimes:[]
    },
    {
      weekday: Weekday.Tuesday,
      dayTimes:[]
    },
    {
      weekday: Weekday.Wednesday,
      dayTimes:[]
    },
    {
      weekday: Weekday.Thursday,
      dayTimes:[]
    },
    {
      weekday: Weekday.Friday,
      dayTimes:[]
    },
    {
      weekday: Weekday.Saturday,
      dayTimes:[]
    },
    {
      weekday: Weekday.Sunday,
      dayTimes:[]
    }
  ];

  selectedRecipeBook:RecipeBookListDto;

  constructor(
    private recipeBookService: RecipeBookService,
    private weekplanService: WeekplanService,
    private router: Router,
    private notification: ToastrService,
    private route: ActivatedRoute){
      const today = new Date();
      const dd = String(today.getDate()).padStart(2, '0');
      const mm = String(today.getMonth() + 1).padStart(2, '0');
      const yyyy = today.getFullYear();
      this.todayDate = `${yyyy}-${mm}-${dd}`;
    }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.createDto.recipeBookId = +params.get('id');
      this.recipeBookService.getRecipeBookDetailsBy(this.createDto.recipeBookId).subscribe({
        next: (data: any) => {
          this.currentRecipeBook = data;
        },
        error: error => {
          console.error('Error getting recipe book.', error);
          this.notification.error('Rezeptbuch konnte nicht geladen werden.', 'Backend Fehler - Rezeptbuch');
        }
      });
    });
  }

  onSubmit(form:Form){
    this.createDto = {
      startDate: null,
      endDate: null,
      weekdays: [],
      recipeBookId: 0
    };
    if(!this.isFormValid()){
      return;
    }
    this.createDto.startDate = new Date(this.fromDate);
    this.createDto.endDate = new Date(this.toDate);
    for (let i = 0; i < this.weekdays.length; i++) {
      let currentWeekday : WeekDayDto = {weekday: "", dayTimes: []};
      currentWeekday.weekday = Object.keys(Weekday).filter((x) => Weekday[x] == this.weekdays[i].weekday)[0];
      for (let j = 0; j < this.weekdays[i].dayTimes.length; j++) {
        currentWeekday.dayTimes.push(Object.keys(Daytime).filter((x) => Daytime[x] == this.weekdays[i].dayTimes[j])[0]);
      }
      this.createDto.weekdays.push(currentWeekday);
    }
    this.createDto.recipeBookId = this.currentRecipeBook.id;

    this.weekplanService.createWeekplan(this.createDto).subscribe({
      next: (data: any) => {
      },
      error: error => {
        {
          console.error('Error creating weekplan.', error);
          this.notification.error('Wochenplan konnte nicht erstellt werden.', 'Backend Fehler - Wochenplan');
        }
      }
    }
  );
  }

  public formatRecipeBook(recipeBook: RecipeBookListDto | null): string {
    return !recipeBook
      ? ""
      : `${recipeBook.name}`
  }

  onCheckboxChange(event:any, day:WeekDayEntity, daytime: Daytime){
    if(event.target.checked){
      this.weekdays[this.weekdays.indexOf(day)].dayTimes.push(daytime);
    } else {
      this.weekdays[this.weekdays.indexOf(day)].dayTimes.splice(
      this.weekdays[this.weekdays.indexOf(day)].dayTimes.indexOf(daytime),1);
    }
  }

  public selectRecipeBook(recipeBook: RecipeBookListDto | null) {
    this.currentRecipeBook = recipeBook;
  }

  isFormValid():boolean{
    if(this.currentRecipeBook === null){
        console.error('Form invalid');
        this.notification.error('Es muss ein Rezeptbuch ausgewählt werden.');
        return false;
    }
    if(this.fromDate === null || this.toDate === null){
      console.error('Form invalid');
        this.notification.error('Es müssen Start- und Enddatum ausgewählt worden sein.');
        return false;
    }
    return true;
  }
}
