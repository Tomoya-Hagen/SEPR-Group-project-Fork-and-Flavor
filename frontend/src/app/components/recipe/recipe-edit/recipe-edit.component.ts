import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { of } from 'rxjs';
import { IngredientComponent } from "../recipe-create/ingredient/ingredient.component";
import { IngredientService } from "../../../services/ingredient.service";
import { DetailedRecipeDto } from 'src/app/dtos/DetailedRecipeDto';
import { SimpleCategory } from 'src/app/dtos/SimpleCategory';
import { SimpleRecipe } from 'src/app/dtos/SimpleRecipe';
import { IngredientDetailDto } from 'src/app/dtos/ingredient';
import { RecipeUpdateDto } from 'src/app/dtos/recipe';

import { RecipeService } from 'src/app/services/recipe.service';
import { Step } from "../../../dtos/Step";
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from 'src/app/services/CategoryService';

@Component({
  selector: 'app-recipe-edit',
  templateUrl: './recipe-edit.component.html',
  styleUrl: './recipe-edit.component.scss'
})
export class RecipeEditComponent implements OnInit {

  error = false;
  errorMessage = '';
  recipe: RecipeUpdateDto = {
    id: 0,
    name: '',
    description: '',
    numberOfServings: 0,
    ingredients: [],
    recipeSteps: [],
    categories: []
  }
  ingbool = false;
  stepbool = false;

  constructor(
    private recipeService: RecipeService,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute
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
    const id = this.route.snapshot.params['id'];
    this.recipeService.getRecipeUpdateDtoById(id).subscribe(recipe => {
      this.recipe = recipe;
    });
    for (let i = 0; i < this.recipe.ingredients.length; i++) {
      this.recipe.ingredients.push({name: this.recipe.ingredients[i].name, id: this.recipe.ingredients[i].id ,amount: this.recipe.ingredients[i].amount, unit: this.recipe.ingredients[i].unit});
    }
    for (let i = 0; i < this.recipe.recipeSteps.length; i++) {
      this.recipe.recipeSteps.push({name: this.recipe.recipeSteps[i].name, description: this.recipe.recipeSteps[i].description, recipeId: this.recipe.recipeSteps[i].recipeId, whichstep: this.recipe.recipeSteps[i].whichstep});
    }
    for (let i = 0; i < this.recipe.categories.length; i++) {
      this.recipe.categories.push({id: this.recipe.categories[i].id, name: this.recipe.categories[i].name});
    }
  }

  public onSubmit(form: NgForm): void {
    console.log(this.recipe);
    if(!this.recipe.recipeSteps[this.recipe.ingredients.length-1].name){
      this.recipe.categories.pop();
    }
    if(this.recipe.categories[this.recipe.ingredients.length-1].name === ""){
      this.recipe.categories.pop();
    }
    if(this.recipe.ingredients[this.recipe.ingredients.length-1].name === ""){
      this.recipe.ingredients.pop();
    }
    this.recipe.recipeSteps.forEach(step => {
      if(step.description){
        delete step.recipeId;
      }
    })

    this.recipeService.updateRecipe(this.recipe).subscribe({
        next: (detrecipe: DetailedRecipeDto) => {
        },
        error: error => {
          this.defaultServiceErrorHandling(error);
        }
      }
    );

    this.router.navigate(['/recipe']);
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
    if(!this.recipe.recipeSteps[index].description){
      this.recipe.recipeSteps[index].whichstep = null;
    }
    else {
      this.recipe.recipeSteps[index].whichstep = true;
    }
    if(this.recipe.recipeSteps.length -1 == index){
      this.recipe.recipeSteps.push(new Step());
    }
  }

  public formatRecipeStep(simpleRecipe: SimpleRecipe | null): string {
    return simpleRecipe?.recipename ?? '';
  }

  public stepchanged(index: number):void{
    if(this.recipe.recipeSteps[index] == null){
      this.recipe.recipeSteps[index] = new Step();
    }
    if(this.recipe.recipeSteps[index].recipeId != 0 && this.recipe.recipeSteps.length -1 == index){
      this.recipe.recipeSteps.push(new Step());
    }

    if(this.recipe.recipeSteps.length > 1 && this.recipe.recipeSteps.slice(0, -1).every(obj => obj != null && obj.whichstep != null)){
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
      this.recipe.categories[index] = {id: 0, name: ""};
    }
    if(this.recipe.categories[index].id != 0 && this.recipe.categories.length -1 == index){
      this.recipe.categories.push({id: 0, name: ""});
    }
  }


}
