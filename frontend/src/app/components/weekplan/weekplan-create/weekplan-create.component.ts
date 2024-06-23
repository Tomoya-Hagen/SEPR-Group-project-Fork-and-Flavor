import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RecipeBookListDto } from 'src/app/dtos/recipe-book';
import { RecipeBookService } from 'src/app/services/recipebook.service';

@Component({
  selector: 'app-weekplan-create',
  templateUrl: './weekplan-create.component.html',
  styleUrl: './weekplan-create.component.scss'
})
export class WeekplanCreateComponent implements OnInit {
  ngOnInit(): void {
      let observable = this.recipeBookService.getRecipeBooksTheUserHasWriteAccessTo();
      observable.subscribe({
        next: data => {
          this.recipeBooks = data;
        },
        error: error => {
          console.error('Error fetching recipe books', error);
          this.notification.error('Rezeptbücher können nicht abgerufen werden.', "Backend Fehler - Rezepte");
          this.router.navigate([''])
        }
      });
  }
  recipeBooks = [];

  constructor(
    private recipeBookService: RecipeBookService,
    private router: Router,
    private notification: ToastrService,){}
}
