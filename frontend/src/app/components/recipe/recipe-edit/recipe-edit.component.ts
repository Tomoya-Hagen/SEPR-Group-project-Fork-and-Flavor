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
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-recipe-edit',
  templateUrl: './recipe-edit.component.html',
  styleUrl: './recipe-edit.component.scss'
})
export class RecipeEditComponent implements OnInit {

  error = false;
  errorMessage = '';
  recipeStepRecipeName = '';
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
  isSubmitDisabled = true;
  isOwner = false;
  ownerId: number = 0;

  constructor(
    private recipeService: RecipeService,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private userService: UserService
  ){}

  ingredientChangeHandler(updatedIngredient: IngredientDetailDto, index: number) {
    console.log(this.recipe.ingredients)
    for (let i = 0; i < this.recipe.ingredients.length; i++) {
      if(i !== index && this.recipe.ingredients[i].id === updatedIngredient.id) {
        this.error = true;
        this.errorMessage = 'Du kannst ein Kategorie nicht doppelt auswählen!';
        this.notification.error(this.errorMessage,"Fehler - Rezept bearbeiten");
        // this.recipe.ingredients.splice(index, 1, { name: "", id: -1, amount: null, unit: null });
        this.validateForm()
        console.log('Duplicate found:', updatedIngredient);
        this.recipe.ingredients[index] = {name: "", id: -1, amount: null, unit: null};
        return;
      }
    }
    this.recipe.ingredients[index] = updatedIngredient;
    if(this.recipe.ingredients[this.recipe.ingredients.length - 1].id != -1){
      this.recipe.ingredients.push({name: "", id: -1, amount: null, unit: null});
    }
    this.validateForm();
  }

  public returnIsSubmitDisabled(): boolean {
    return this.isSubmitDisabled;
  }


  ngOnInit(): void {
    const recipeId = this.route.snapshot.params['id']
    this.recipeService.getRecipeDetailsBy(recipeId).subscribe({
      next: (recipe) => {
        this.ownerId = recipe.ownerId;
        this.userService.getCurrentUser().subscribe({
          next: (currentUser) => {
            if (this.ownerId === currentUser.id) {
              this.isOwner = true;
              this.recipeService.getRecipeUpdateDtoById(recipeId).subscribe({
                next: (recipe) => {
                  this.recipe = recipe;
                },
                error: (err) => {
                  console.error('Error loading recipe details:', err);
                  this.navToDetails(recipeId);
                }
              });
            } else {
              this.navToDetails(recipeId);
            }
          },
          error: (err) => {
            console.error('Error fetching current user:', err);
            this.navToDetails(recipeId);
          }
        });
      },
      error: (err) => {
        console.error('Error fetching recipe details:', err);
        this.navToDetails(recipeId);
      }
    });
  }
  public onSubmit(form: NgForm): void {
    console.log(this.recipe);
    for (let i = 0; i < this.recipe.recipeSteps.length; i++) {
      if(this.recipe.recipeSteps[i].name === ""){
        this.recipe.recipeSteps.splice(i, 1);
        i--;
      }
    }
    console.log(this.recipe.ingredients.length);
    for (let i = 0; i < this.recipe.ingredients.length; i++) {
      if(this.recipe.ingredients[i].id <= 0){
        this.recipe.ingredients.splice(i, 1);
        i--;
      }
    }
    console.log(this.recipe.ingredients.length);
    for (let i = 0; i < this.recipe.categories.length; i++) {
      if(this.recipe.categories[i].id <= 0){
        this.recipe.categories.splice(i, 1);
        i--;
      }
    }
    this.recipe.recipeSteps.forEach(step => {
      if(step.description){
        delete step.recipeId;
      }
    })

    this.recipeService.updateRecipe(this.recipe).subscribe({
      next: (detrecipe: DetailedRecipeDto) => {
        // this.notification.info('Update successful!');
        this.router.navigate(['recipe', 'details', this.recipe.id]);
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
      this.notification.error('Rezept kann nicht aktualisiert werden:' + this.errorMessage.toString(), 'Backend Fehler - Rezepte bearbeiten');
    } else {
      this.errorMessage = error.error;
      this.notification.error( 'Rezept kann nicht aktualisiert werden:' + this.errorMessage.toString(), 'Backend Fehler - Rezepte bearbeiten');
    }
  }

  vanishError() {
    this.error = false;
  }


  public textareachange(index : number) : void{
    if(!this.recipe.recipeSteps[index].description){
      this.recipe.recipeSteps[index].whichstep = null;
    } else {
      this.recipe.recipeSteps[index].whichstep = true;
    }
    if(this.recipe.recipeSteps.length -1 == index){
      this.recipe.recipeSteps.push(new Step());
    }
    this.validateForm();
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

    if (this.recipe.recipeSteps[index].recipeId === this.recipe.id) {
      this.error = true;
      this.errorMessage = 'Ein Rezept kann sich selbst nicht als Rezeptschritt referenzieren.';
      this.recipe.recipeSteps[index] = new Step();
      this.notification.error(this.errorMessage,"Rezept Bearbeiten - Fehler");
      return;
    }

    this.validateForm();
  }

  recipeStepSuggestions = (input: string) => (input === '')
    ? of([])
    :  this.recipeService.recipeByName(input, 0,5);


  public formatCategory(simpleCategory: SimpleCategory | null): string {
    return simpleCategory?.name ?? '';
  }

  categorySuggestions = (input: string) => (input === '')
    ? of([])
    :  this.categoryService.categoryByName(input, 5);

  public categorychanged(index: number): void {
    if(this.recipe.categories[index] == null){
      this.recipe.categories[index] = {id: 0, name: ""};
    }
    if(this.recipe.categories[index].id != 0 && this.recipe.categories.length - 1 == index){
      this.recipe.categories.push({id: 0, name: ""});
    }
    this.validateForm();
  }

  public validateForm(): void {
    this.ingbool = this.recipe.ingredients.length > 1 && this.recipe.ingredients.slice(0, -1).every(ingredient =>
      ingredient != null && ingredient.id > 0 && ingredient.amount != null && ingredient.unit != null
    );
    for (let i = 0; i < this.recipe.ingredients.length; i++) {
      if(this.recipe.ingredients[i] && this.recipe.ingredients[i].name && this.recipe.ingredients[i].name != "") {
        this.ingbool = true;
      }
    }

    this.stepbool = this.recipe.recipeSteps.slice(0, -1).every(step =>
      step != null && step.whichstep != null && (step.whichstep === true || step.whichstep === false)
    );

    this.stepbool = this.recipe.recipeSteps.some(step =>
      (step.description && step.name && step.name.trim() !== '' && step.description.trim() !== '') || (step.recipeId && step.recipeId > 0)
    );

    this.isSubmitDisabled = !(this.recipe.name && this.recipe.description && this.ingbool && this.stepbool);
  }

  public removeStep(index: number) {
    this.recipe.recipeSteps.splice(index, 1);
    if (this.recipe.recipeSteps.length === 0) {
      this.recipe.recipeSteps.push(new Step());
    }
  }

  public removeIngredient(index: number) {
    this.recipe.ingredients.splice(index, 1);
    if (this.recipe.ingredients.length === 0) {
      this.recipe.ingredients.push({ name: "", id: -1, amount: null, unit: null });
    }
  }
  public removeCategory(index: number) {
    this.recipe.categories.splice(index, 1);
    if (this.recipe.categories.length === 0) {
      this.recipe.categories.push({ id: 0, name: "" });
    }
  }

  public navToDetails(recipeId: number) {
    this.router.navigate(['recipe', 'details', recipeId]);
  }

}
