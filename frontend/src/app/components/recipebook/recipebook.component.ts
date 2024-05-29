import {Component, OnInit} from '@angular/core';
import {RecipeBookService} from "../../services/recipebook.service";
import {ToastrService} from "ngx-toastr";
import {RecipeBookListDto, RecipeBookSearch} from "../../dtos/recipe-book";
import {NgForOf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {CardComponent} from "../card/card.component";

@Component({
  selector: 'app-recipebook',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink,
    FormsModule,
    CardComponent
  ],
  templateUrl: './recipebook.component.html',
  styleUrl: './recipebook.component.scss'
})

export class RecipebookComponent implements OnInit {

  steps = [1, 10, 25, 50, 100];
  bannerError: string | null = null;
  page = 0;
  step = 10;
  oldStep = 10;
  previousButtonDisabled = false;
  nextButtonDisabled = false;
  data: RecipeBookListDto[] = [];
  bookSearch: RecipeBookSearch = new class implements RecipeBookSearch {
    name: string;
  };
  constructor(
    private service: RecipeBookService,
    private notification: ToastrService,
  ) {
  }

  ngOnInit(): void {
    this.loadNextPage()
  }

  loadPage(isNext: boolean) {
    console.log("Trying to get all recipe books.");
    this.service.getAllRecipeBooksBySteps(this.page, this.step).subscribe({
      next: items => {
        console.log("Successfully received all recipe books");
        if (items.length == 0) {
          if (isNext) {
            this.nextButtonDisabled = true;
          } else {
            this.previousButtonDisabled = true;
          }
          return;
        }
        this.data = items;
        },
      error: error => {
        console.error('Error fetching recipes', error);
        this.bannerError = 'Could not fetch recipes: ' + error.message;
        const errorMessage = error.status === 0
          ? 'Is the backend up?'
          : error.message.message;
        this.notification.error(errorMessage, 'Could Not Fetch Recipes');
      }})
  }

  searchChanged(): void {
    console.log("Trying to get the searched recipe books.");
    this.service.search(this.bookSearch).subscribe({
      next: items => {
        console.log("Successfully received all recipe books");
        this.data = items;

      },
      error: error => {
        console.error('Error fetching recipes', error);
        this.bannerError = 'Could not fetch recipe books: ' + error.message;
        const errorMessage = error.status === 0
          ? 'Is the backend up?'
          : error.message.message;
        this.notification.error(errorMessage, 'Could Not Fetch Recipes');
      }})
  }

  loadNextPage() {
    this.nextButtonDisabled = false;
    this.previousButtonDisabled = false;
    this.page += 1;
    this.loadPage(true);
  }

  loadPreviousPage() {
    this.nextButtonDisabled = false;
    this.previousButtonDisabled = false;
    this.page -= 1;
    this.loadPage(false)
  }

  selectedStepChanged() {
    let recipeNumber = this.step * this.page;
    this.page = Math.floor(recipeNumber / this.step);
    this.oldStep = this.step;
    this.loadPage(true);
  }

}
