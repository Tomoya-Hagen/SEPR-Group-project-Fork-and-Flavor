import { Component, OnInit} from '@angular/core';
import {Recipe} from "../../../dtos/Recipe";
import {NgForm} from "@angular/forms";
import {IngredientComponent} from "./ingredient/ingredient.component";
import {Ingredient} from "../../../dtos/Ingredient";
import {of} from "rxjs";
import {IngredientService} from "../../../services/ingredient.service";
import {RecipeService} from "../../../services/RecipeService";
import {Step} from "../../../dtos/Step";
import {SimpleRecipe} from "../../../dtos/SimpleRecipe";
import {SimpleCategory} from "../../../dtos/SimpleCategory";
import {CategoryService} from "../../../services/CategoryService";

@Component({
  selector: 'app-recipe-create',
  templateUrl: './recipe-create.component.html',
  styleUrl: './recipe-create.component.scss'
})
export class RecipeCreateComponent implements OnInit{

  recipe: Recipe = {
    name: '',
    description: '',
    servings: 0,
    ownerid: -1,
    ingredients: [],
    steps: [],
    category: []
  }

  constructor(
    private recipeService: RecipeService,
    private categoryService: CategoryService,

  ){}

  ingredientChangeHandler(updatedIngredient: Ingredient, index: number) {
    this.recipe.ingredients[index] = updatedIngredient;
    if(this.recipe.ingredients[this.recipe.ingredients.length-1].id != -1){
      this.recipe.ingredients.push({name: "", id: -1,amount: 0, unit:"g"});
    }
  }


  ngOnInit(): void {
    this.recipe.ingredients.push({name: "", id: -1,amount: 0, unit:"g"});
    this.recipe.steps.push(new Step());
    this.recipe.category.push({id: 0, name: ""});
    console.log(this.recipe.steps);
  }

  public onSubmit(form: NgForm): void {
    this.recipe.steps.pop();
    this.recipe.category.pop();
    this.recipe.ingredients.pop();
    this.recipe.steps.forEach(step => {
      if(step.description){
        delete step.recipeId;
      }
    })
    this.recipe.category.forEach(cat => {
      delete cat.name;
    })
    console.log(this.recipe);
  }



  public textareachange(index : number) : void{
    console.log(this.recipe.steps[index].description);
    if(!this.recipe.steps[index].description){
      this.recipe.steps[index].whichstep = null;
    }
    else {
      this.recipe.steps[index].whichstep = true;
    }
    if(this.recipe.steps.length -1 == index){
      this.recipe.steps.push(new Step());
    }
  }

  public formatRecipeStep(simpleRecipe: SimpleRecipe | null): string {
    return simpleRecipe?.recipename ?? '';
  }

  public stepchanged(index: number):void{
    if(this.recipe.steps[index] == null){
      this.recipe.steps[index] = new Step();
    }
    if(this.recipe.steps[index].recipeId != 0 && this.recipe.steps.length -1 == index){
      this.recipe.steps.push(new Step());
    }
  }

  recipeStepSuggestions = (input: string) => (input === '')
    ? of([])
    :  this.recipeService.recipeByName(input, 5);



  public formatCategory(simpleCategory: SimpleCategory | null): string {
    return simpleCategory?.name ?? '';
  }

  categorySuggestions = (input: string) => (input === '')
    ? of([])
    :  this.categoryService.categoryByName(input, 5);

  public categorychanged(index: number):void{
    console.log(this.recipe.category)
    if(this.recipe.category[index] == null){
      this.recipe.category[index] = {id: 0, name: ""}
    }
    if(this.recipe.category[index].id != 0 && this.recipe.category.length -1 == index){
      this.recipe.category.push({id: 0, name: ""});
    }
  }
}

