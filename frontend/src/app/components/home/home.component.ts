import { Component, OnInit } from '@angular/core';
import { RecipeService } from 'src/app/services/recipe.service';
import {Recipe} from 'src/app/dtos/recipe';
import {RecipeBook, RecipeBookListDto} from "../../dtos/recipe-book";
import {RecipeBookService} from "../../services/recipebook.service";
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  recipes: Recipe[] = [];
  recipeBooks: RecipeBookListDto[] = [];
  recommended: Recipe[] = [];

  constructor(
    private recipeService: RecipeService,
    private recipeBookService: RecipeBookService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getBestRecipes();
    this.getBestRecipeBooks();
    this.getRecommendedRecipes()
  }

  getRecommendedRecipes(){
    this.recipeService.getRecommendedRecipes()
      .subscribe(data => {
        this.recommended = data;
      });
  }

  getRecipes(): void {
    this.recipeService.getRecipes('', 0,0, 6)
      .subscribe(data => {
        this.recipes = data.content;
      });
  }

  getRecipeBooks(): void {
    this.recipeBookService.search('', 0, 6)
      .subscribe(data => {
      this.recipeBooks = data.content;
    });
  }

  getBestRecipes(): void {
    this.recipeService.getBestRecipes(6)
      .subscribe(data => {
        this.recipes = data.content;
      });
  }

  getBestRecipeBooks(): void {
    this.recipeBookService.getBestRecipeBooks( 6)
      .subscribe(data => {
        this.recipeBooks = data.content;
      });
  }

  navigateToRecipeSearch(): void {
    this.router.navigate(['/recipe']);
  }

}
