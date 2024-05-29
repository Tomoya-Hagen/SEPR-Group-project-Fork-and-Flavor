import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { RecipeService } from 'src/app/services/recipe.service';
import { RecipeListDto } from 'src/app/dtos/recipe';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  steps = [3, 9, 30, 90, 300];
  page = 1;
  step = 9;
  oldStep = 9;
  previousButtonDisabled = true;
  nextButtonDisabled = false;
  recipes: RecipeListDto[] = [];
  bannerError: string | null = null;

  constructor(
    public authService: AuthService,
    private recipeService: RecipeService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadRecipes();
  }

  loadRecipes(isNext: boolean = true): void {
    this.recipeService.getListByPageAndStep(this.page, this.step).subscribe({
      next: (data) => {
        this.handleDataResponse(data, isNext);
      },
      error: (error) => {
        this.handleError(error);
      }
    });
  }

  handleDataResponse(data: RecipeListDto[], isNext: boolean): void {
    if (data.length === 0) {
      this.updateButtonDisabledState(isNext, true);
      return;
    }
    this.recipes = data;
    this.updateButtonDisabledState(isNext, false);
  }

  handleError(error: any): void {
    console.error('Error fetching recipes', error);
    this.bannerError = 'Could not fetch recipes: ' + error.message;
  }

  loadNextPage(): void {
    this.page += 1;
    this.updateNavigationButtons();
    this.loadRecipes(true);
  }

  loadPreviousPage(): void {
    if (this.page > 1) {
      this.page -= 1;
    }
    this.updateNavigationButtons();
    this.loadRecipes(false);
  }

  selectedStepChanged(): void {
    if (this.oldStep !== this.step) {
      const recipeNumber = this.oldStep * (this.page - 1); // calculate the first recipe index on the current page
      this.page = Math.floor(recipeNumber / this.step) + 1; // adjust page number based on new step
      this.oldStep = this.step; // Update oldStep to current step after adjustment
      this.loadRecipes(true); // Load the new page with the updated step
    }
  }

  private updateNavigationButtons(): void {
    this.previousButtonDisabled = this.page === 1;
    this.nextButtonDisabled = false; // Reset to allow next page attempt
  }

  private updateButtonDisabledState(isNext: boolean, disable: boolean): void {
    if (isNext) {
      this.nextButtonDisabled = disable;
    } else {
      this.previousButtonDisabled = disable;
    }
  }
}
