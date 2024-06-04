import { Component, OnInit } from '@angular/core';
import { RecipeBookService } from "../../services/recipebook.service";
import { ToastrService } from "ngx-toastr";
import { NgForOf } from "@angular/common";
import { RouterLink } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { CardComponent } from "../card/card.component";
import { RecipeBookListDto, RecipeBookSearch } from "../../dtos/recipe-book";

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
  styleUrls: ['./recipebook.component.scss']
})
export class RecipebookComponent implements OnInit {
  steps = [3, 6, 9, 30];
  page = 1;
  step = 6;
  oldStep = 3;
  previousButtonDisabled = true;
  nextButtonDisabled = false;
  bannerError: string | null = null;
  data: RecipeBookListDto[] = [];
  bookSearch: RecipeBookSearch = { name: '' };

  constructor(
    private service: RecipeBookService,
    private notification: ToastrService,
  ) {}

  ngOnInit(): void {
    this.loadRecipes();
  }

  loadRecipes(isNext: boolean = true) {
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
    this.page += 1;
    this.updateNavigationButtons();
    this.loadRecipes(true);
  }

  loadPreviousPage() {
    if (this.page > 1) {
      this.page -= 1;
    }
    this.updateNavigationButtons();
    this.loadRecipes(false);
  }

  selectedStepChanged() {
    let recipeNumber = this.step * this.page;
    this.page = Math.floor(recipeNumber / this.step);
    this.oldStep = this.step;
    this.loadRecipes(true);
  }

  private updateNavigationButtons(): void {
    this.previousButtonDisabled = this.page === 1;
    this.nextButtonDisabled = false; // Reset to allow next page attempt
  }

  private handleLoadError(error: any) {
    console.error('Error fetching recipes', error);
    const errorMessage = error.status === 0 ? 'Is the backend up?' : error.message.message;
    this.notification.error(errorMessage, 'Could Not Fetch Recipes');
    this.bannerError = 'Could not fetch recipes: ' + error.message;
  }
}
