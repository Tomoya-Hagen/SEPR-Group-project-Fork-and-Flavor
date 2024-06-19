import { Component, OnInit, OnDestroy } from '@angular/core';
import { RecipeService } from '../../services/recipe.service';
import { Recipe, RecipeListDto, RecipeSearch } from '../../dtos/recipe';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CategoryService } from "../../services/CategoryService";
import { CategoryDetailDto } from "../../dtos/category";

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
    name: '',
    categoryId: 0,
  };
  pagedData: RecipeListDto[] = [];
  searchSubscription: Subscription;
  selected: boolean;

  recipe: Recipe = {
    name: '',
    description: '',
    numberOfServings: 0,
    ingredients: [],
    recipeSteps: [],
    categories: []
  }

  show = {
    ingredients: [],
    category: [],
    recipestep: [],
    units: []
  }

  possible = {
    ingredients: [],
    category: [],
    recipestep: [],
    units: []
  }

  input = {
    ingredient: '',
    category: '',
    recipestep: []
  }

  constructor(
    private service: RecipeService,
    private notification: ToastrService,
    private categoryService: CategoryService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.searchChanged();
    this.categoryService.allcategories().subscribe({
      next: (response) => {
        this.possible.category = response;
        this.show.category = response;
      }
    });
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
          this.notification.error('Rezepte können nicht abgerufen werden.', 'Backend Fehler - Rezepte');
        }
      });
  }

  public categoryChanged(): void {
    this.show.category = this.possible.category.filter(obj => obj.name.toLowerCase().includes(this.input.category.toLowerCase()));
    this.searchChanged();
  }

  selectcategory(category: CategoryDetailDto): void {
    if (!this.recipe.categories.includes(category) && !this.selected) {
      this.recipe.categories.push(category);
      this.selected = true;
      this.recipeSearch.categoryId = category.id;
      this.searchChanged();
    } else {
      this.notification.error('Bitte löschen Sie bereits ausgewählte Kategorie, um neue Kategorie zu wählen.', 'Error bei Filter von Kategorien');
    }
  }

  deletecategory(id: number): void {
    this.recipe.categories.splice(id, 1);
    this.selected = false;
    this.back();
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

  back(): void {
    this.recipeSearch.name = '';
    this.recipeSearch.categoryId = 0;
    this.searchSubscription = this.service.search(this.recipeSearch, this.page - 1, this.size)
      .subscribe({
        next: (data: any) => {
          this.pagedData = data.content;
          this.totalElements = data.totalElements;
        }
      });
  }

  showAllCategories(): void {
    this.show.category = this.possible.category;
  }
}
