import { Component, OnInit, OnDestroy } from '@angular/core';
import { RecipeService } from '../../services/recipe.service';
import { RecipeListDto, RecipeSearch } from '../../dtos/recipe';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit, OnDestroy {
  data: RecipeListDto[] = [];
  totalElements: number;
  page: number = 1;
  size: number = 24;
  pageSizes: number[] = [3, 9, 24, 90, 300, 600];
  recipeSearch: RecipeSearch = {
    name: ''
  };
  pagedData: RecipeListDto[] = [];
  searchSubscription: Subscription;

  constructor(
    private service: RecipeService,
    private notification: ToastrService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.searchChanged();
  }

  ngOnDestroy(): void {
    if (this.searchSubscription) {
      this.searchSubscription.unsubscribe();
    }
  }

  searchChanged(): void {
    if (this.searchSubscription) {
      this.searchSubscription.unsubscribe();
    }
    this.searchSubscription = this.service.search(this.recipeSearch, this.page - 1, this.size)
      .subscribe({
        next: (data: any) => {
          this.pagedData = data.content;
          this.totalElements = data.totalElements;
        },
        error: error => {
          console.error('Error fetching recipes.', error);
          this.notification.error('Could not fetch recipes.', 'Backend Error - Recipe');
        }
      });
  }

  onPageChange(pageNumber: number): void {
    this.page = pageNumber;
    this.searchChanged();
  }

  onPageSizeChange(event: Event): void {
    this.size = +(event.target as HTMLSelectElement).value;
    this.page = 1; // Reset to first page
    this.searchChanged();
  }

  detail(id: number): void {
    this.router.navigate(['/recipe/details', id]);
  }
}
