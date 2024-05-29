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
  steps = [3, 9, 30, 90, 300];
  page = 1;
  step = 300;
  oldStep = 300;
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
    this.updateButtonState();
    this.service.getAllRecipeBooksBySteps(this.page, this.step).subscribe({
      next: items => {
        this.data = items;
        this.updateButtonState(items.length > 0, isNext);
      },
      error: error => this.handleLoadError(error)
    });
  }

  searchChanged(): void {
    this.service.search(this.bookSearch).subscribe({
      next: items => this.data = items,
      error: error => this.handleLoadError(error)
    });
  }

  loadNextPage() {
    this.page++;
    this.loadRecipes(true);
  }

  loadPreviousPage() {
    if (this.page > 1) {
      this.page--;
      this.loadRecipes(false);
    }
  }

  selectedStepChanged() {
    const recipeNumber = this.oldStep * (this.page - 1) + this.step;
    this.page = Math.floor(recipeNumber / this.step) + 1;
    this.oldStep = this.step;
    this.loadRecipes();
  }

  private updateButtonState(hasData: boolean = true, isNext: boolean = true) {
    if (isNext) {
      this.nextButtonDisabled = !hasData;
    } else {
      this.previousButtonDisabled = this.page <= 1;
    }
  }

  private handleLoadError(error: any) {
    console.error('Error fetching recipes', error);
    const errorMessage = error.status === 0 ? 'Is the backend up?' : error.message.message;
    this.notification.error(errorMessage, 'Could Not Fetch Recipes');
    this.bannerError = 'Could not fetch recipes: ' + error.message;
  }
}
