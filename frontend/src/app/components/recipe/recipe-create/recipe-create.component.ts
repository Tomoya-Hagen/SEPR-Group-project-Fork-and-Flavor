import { Component, OnInit} from '@angular/core';
import {Recipe} from "../../../dtos/recipe";
import {AbstractControl, NgForm, ValidationErrors, ValidatorFn} from "@angular/forms";
import {IngredientComponent} from "./ingredient/ingredient.component";
import {of} from "rxjs";
import {IngredientService} from "../../../services/ingredient.service";
import {RecipeService} from "../../../services/recipe.service";
import {Step} from "../../../dtos/Step";
import {SimpleRecipe} from "../../../dtos/SimpleRecipe";
import {SimpleCategory} from "../../../dtos/SimpleCategory";
import {CategoryService} from "../../../services/CategoryService";
import {DetailedRecipeDto} from "../../../dtos/DetailedRecipeDto";
import {IngredientDetailDto} from "../../../dtos/ingredient";

@Component({
  selector: 'app-recipe-create',
  templateUrl: './recipe-create.component.html',
  styleUrl: './recipe-create.component.scss'
})
export class RecipeCreateComponent implements OnInit{

  error = false;
  errorMessage = '';
  recipe: Recipe = {
    name: '',
    description: '',
    servings: 0,
    ownerId: -1,
    ingredients: [],
    steps: [],
    categories: []
  }
  ingbool = false;
  stepbool = false;

  constructor(
    private recipeService: RecipeService,
    private categoryService: CategoryService,

  ){}

  ingredientChangeHandler(updatedIngredient: IngredientDetailDto, index: number) {
    console.log(this.recipe.ingredients)
    this.recipe.ingredients[index] = updatedIngredient;
    if(this.recipe.ingredients[this.recipe.ingredients.length-1].id != -1){
      this.recipe.ingredients.push({name: "", id: -1,amount: null, unit:null});
    }
    if(this.recipe.ingredients.length > 1 && this.recipe.ingredients.slice(0, -1).every(obj => obj != null && obj.id !== -1 && obj.id !== 0)){
      this.ingbool = true;
    }
    else {
      this.ingbool = false;
    }
  }


  ngOnInit(): void {
    this.recipe.ingredients.push({name: "", id: -1,amount: 0, unit:"g"});
    this.recipe.steps.push(new Step());
    this.recipe.categories.push({id: 0});
  }

  public onSubmit(form: NgForm): void {
    console.log(this.recipe);
    this.recipe.steps.pop();
    this.recipe.categories.pop();
    this.recipe.ingredients.pop();
    this.recipe.steps.forEach(step => {
      if(step.description){
        delete step.recipeId;
      }
    })
    let retrecipe = this.recipeService.createRecipe(this.recipe);

    this.recipeService.createRecipe(this.recipe).subscribe({
        next: (detrecipe: DetailedRecipeDto) => {
        },
        error: error => {
          this.defaultServiceErrorHandling(error);
        }
      }
    );
  }


  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

  vanishError() {
    this.error = false;
  }


  public textareachange(index : number) : void{
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

    if(this.recipe.steps.length > 1 && this.recipe.steps.slice(0, -1).every(obj => obj != null && obj.whichstep != null)){
      this.stepbool = true;
    }
    else {
      this.stepbool = false;
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
    if(this.recipe.categories[index] == null){
      this.recipe.categories[index] = {id: 0}
    }
    if(this.recipe.categories[index].id != 0 && this.recipe.categories.length -1 == index){
      this.recipe.categories.push({id: 0});
    }
  }

  public fill() {
    let rec = {
      name: "A",
      description: "B",
      servings: 1,
      ownerId: 1,
      ingredients: [
        {
          id: 1,
          name: "Ananas",
          amount: 5,
          unit: "g"
        },
        {
          id: 133,
          name: "Salz",
          amount: 10,
          unit: "kg"
        }
      ],
      steps: [
        {
          name: "1",
          description: "1",
          whichstep: true,
        },
        {
          recipeId: 2,
          whichstep: false,
          name: "2"
        }
      ],
      categories: [
        {
          id: 2
        },
        {
          id: 6
        }
      ]
    }
    this.recipe=rec;
  }


}



