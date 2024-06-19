import {Component, Input, Output, EventEmitter, OnInit} from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {Recipe, RecipeListDto} from 'src/app/dtos/recipe';
import {RecipeService} from "../../../../services/recipe.service";
import {Page} from "../../../../models/page.model";

@Component({
  selector: 'app-recipe-modal',
  templateUrl: './recipe-modal.component.html',
  styleUrls: ['./recipe-modal.component.scss']
})
export class RecipeModalComponent implements OnInit{
  @Input() recipeId: number;
  @Output() updateRecipes = new EventEmitter<Recipe[]>();

  recipes: Recipe[] = [];
  newRecipeName: string = '';
  filteredRecipes: Recipe[] = [];
  page: number = 0;
  size: number = 10;

  constructor(
    public activeModal: NgbActiveModal,
    private recipeService: RecipeService
  ) { }

  ngOnInit() {
    this.loadAllRecipesThatGoWellWith();
  }

  loadAllRecipesThatGoWellWith(page: number = 0) {
    this.recipeService.getGoesWellWith(this.recipeId, page, this.size).subscribe((pageResult: Page<Recipe>) => {
      this.recipes = this.recipes.concat(pageResult.content);
      if (!pageResult.last) {
        this.loadAllRecipesThatGoWellWith(page + 1);
      }
    });
  }

  loadRecipes() {
    if (this.newRecipeName.trim()) {
      this.recipeService.getRecipes(this.newRecipeName, this.page, this.size).subscribe((page: Page<Recipe>) => {
        this.filteredRecipes = page.content.filter(recipe =>
          !this.recipes.some(r => r.name === recipe.name)
        );
      });
    } else {
      this.filteredRecipes = [];
    }
  }

  removeRecipe(recipe: Recipe) {
    this.recipes = this.recipes.filter(r => r !== recipe);
    this.updateRecipes.emit(this.recipes);
  }

  recipeNameChanged() {
    this.loadRecipes();
  }

  selectRecipe(recipe: Recipe) {
    this.recipes.push(recipe);
    this.newRecipeName = '';
    this.filteredRecipes = [];
    this.updateRecipes.emit(this.recipes);
  }

}
