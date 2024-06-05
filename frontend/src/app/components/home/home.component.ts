import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { RecipeService } from 'src/app/services/recipe.service';
import {Recipe, RecipeListDto} from 'src/app/dtos/recipe';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {Page} from "../../models/page.model";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  recipes: Recipe[] = [];
  totalElements: number;
  page: number = 1;
  size: number = 9;
  bannerError: string | null = null;
  pageSizes: number[] = [3, 9, 30, 90];

  constructor(
    public authService: AuthService,
    private recipeService: RecipeService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.getRecipes(this.page);
  }

  getRecipes(page: number): void {
    this.recipeService.getRecipes('', page - 1, this.size) // Backend pagination is 0-based
      .subscribe(data => {
        this.recipes = data.content;
        this.totalElements = data.totalElements;
        this.page = data.number + 1; // ng-bootstrap pagination is 1-based
      });
  }

  onPageSizeChange(newSize: number): void {
    this.size = newSize;
    this.getRecipes(1); // Reset to first page when changing page size
  }

  handleError(error: any): void {
    console.error('Error fetching recipes', error);
    this.bannerError = 'Could not fetch recipes: ' + error.message;
  }
}
