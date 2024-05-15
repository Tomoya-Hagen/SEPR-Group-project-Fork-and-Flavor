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
  steps = [1, 10, 25, 50, 100];
  bannerError: string | null = null;
  page = 0;
  step = 10;
  oldStep = 10;
  previousButtonDisabled = false;
  nextButtonDisabled = false;
  recipes: RecipeListDto[] = [];
  constructor(
    public authService: AuthService,
    private service: RecipeService,
    private router: Router,
    private notification: ToastrService
  ) { }

  ngOnInit() {
    this.loadNextPage();
  }

  loadPage(isNext: boolean) {
    this.service.getListByPageAndStep(this.page, this.step)
      .subscribe({
        next: data => {
          if (data.length == 0) {
            if (isNext) {
              this.nextButtonDisabled = true;
            } else {
              this.previousButtonDisabled = true;
            }
            return;
          }
          this.recipes = data;
        },
        error: error => {
          console.error('Error fetching recipes', error);
          this.bannerError = 'Could not fetch recipes: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Recipes');
        }
      });
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
