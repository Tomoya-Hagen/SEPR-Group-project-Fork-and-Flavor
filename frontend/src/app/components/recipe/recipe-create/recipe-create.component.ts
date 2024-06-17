import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Recipe} from "../../../dtos/recipe";
import {AbstractControl, NgForm, ValidationErrors, ValidatorFn} from "@angular/forms";
import {IngredientComponent} from "./ingredient/ingredient.component";
import {catchError, of} from "rxjs";
import {IngredientService} from "../../../services/ingredient.service";
import {RecipeService} from "../../../services/recipe.service";
import {Step} from "../../../dtos/Step";
import {SimpleRecipe} from "../../../dtos/SimpleRecipe";
import {SimpleCategory} from "../../../dtos/SimpleCategory";
import {CategoryService} from "../../../services/CategoryService";
import {DetailedRecipeDto} from "../../../dtos/DetailedRecipeDto";
import {IngredientDetailDto} from "../../../dtos/ingredient";
import {AuthService} from '../../../services/auth.service';
import {tap} from "rxjs/operators";
import {ActivatedRoute, Router} from '@angular/router';
import index from "eslint-plugin-jsdoc";
import {CategoryDetailDto} from "../../../dtos/category";
import {ToastrService} from "ngx-toastr";


@Component({
  selector: 'app-recipe-create',
  templateUrl: './recipe-create.component.html',
  styleUrl: './recipe-create.component.scss'
})
export class RecipeCreateComponent implements OnInit {

  error = false;
  errorMessage = '';
  recipe: Recipe = {
    name: '',
    description: '',
    numberOfServings: 0,
    ingredients: [],
    recipeSteps: [],
    categories: []
  }
  ratings=[0,1,2,3,4,5];
  ingbool = false;
  stepbool = false;

  show = {
    ingredients: [],
    category: [],
    recipestep:[],
    units: []
  }
  possible = {
    ingredients: [],
    category: [],
    recipestep:[],
    units: []
  }

  input = {
    ingredient: '',
    category : '',
    recipestep: []
  }
  mode: 'create' | 'edit' | "fork" = 'create';

  constructor(
    private recipeService: RecipeService,
    private categoryService: CategoryService,
    private authService: AuthService,
    private ingredientService: IngredientService,
    private router: Router,
    private route: ActivatedRoute,
    private changedetec: ChangeDetectorRef,
    private notification: ToastrService
  ) {
  }

  ingredientChangeHandler(updatedIngredient: IngredientDetailDto, index: number) {
    console.log(this.recipe.ingredients)
    this.recipe.ingredients[index] = updatedIngredient;
    if (this.recipe.ingredients[this.recipe.ingredients.length - 1].id != -1) {
      this.recipe.ingredients.push({name: "", id: -1, amount: null, unit: null});
    }
    if (this.recipe.ingredients.length > 1 && this.recipe.ingredients.slice(0, -1).every(obj => obj != null && obj.id !== -1 && obj.id !== 0)) {
      this.ingbool = true;
    } else {
      this.ingbool = false;
    }
  }


  ngOnInit(): void {
    this.authService.isLogged()
      .pipe(
        tap((isLoggedIn: boolean) => {
          console.log('Is logged in:', isLoggedIn);
        }),
        catchError((error) => {
          console.error('Error:', error);
          this.notification.error('Sie müssen sich als Benutzer anmelden oder als Benutzer registrieren, um ein Rezept zu erstellen.', 'Rezept kann nicht erstellt werden.');
          this.router.navigate(['/login']);
          return of(false); // Handle the error and return a fallback value
        })
      )
      .subscribe();

    console.log(this.route.snapshot.url[0].path === "fork")
    const id = this.route.snapshot.params['id'];
    if(!id){
      this.mode = "create";
    } else if (this.route.snapshot.url[0].path === "edit"){
      this.mode = "edit";
    } else if(this.route.snapshot.url[0].path === "fork"){
      this.mode = "fork";
    } else{
     this.router.navigate(["/home"]);
    }

    this.ingredientService.allingredients().subscribe({
      next:(response) => {
        this.possible.ingredients = response
        this.show.ingredients = response
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });

    this.categoryService.allcategories().subscribe({
      next:(response) => {
        this.possible.category = response
        this.show.category = response
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });

    this.recipeService.recipeByName('',999).subscribe({
      next:(response) => {
        this.possible.recipestep = response
        this.show.recipestep = response
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });

    if(this.mode == 'edit' || this.mode == "fork"){
      this.recipeService.getRecipeEditDtoById(id).subscribe(recipe => {
        this.recipe.name = recipe.name;
        this.recipe.description = recipe.description;
        this.recipe.numberOfServings = recipe.numberOfServings;
        this.recipe.ingredients = recipe.ingredients;
        this.recipe.categories = recipe.categories;
        for (let step of recipe.recipeSteps){
          let count = 0;
          if(!step.whichstep){
            this.recipe.recipeSteps.push({name: step.name, recipeId: step.recipeId,whichstep: step.whichstep})
            this.input.recipestep[count] = this.possible.recipestep[step.recipeId-1].recipename;
          } else {
            this.recipe.recipeSteps.push({name: step.name, description: step.description,whichstep: step.whichstep})
          }
          count++;
        }
      });
    }


    this.ingredientService.allunits().subscribe({
      next:(response) => {
        this.possible.units = response
        this.show.units = response
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  public onSubmit(form: NgForm): void {
    this.recipe.recipeSteps.forEach(step => {
      if(step.description){
        delete step.recipeId;
      }
      if(step.recipeId){
        step.description = "";
      }
    })

    if(this.mode === "create"){
      this.recipeService.createRecipe(this.recipe).subscribe({
        next: (detrecipe: DetailedRecipeDto) => {
          this.notification.success("Das Rezept wurde erfolgreich erstellt.", "Rezepte erstellen erfolgreich!");
          this.router.navigate(['/recipe/details/' + detrecipe.id]);
        },
        error: error => {
          this.defaultServiceErrorHandling(error);
        }
      }
      );
    } else if(this.mode === "edit"){
      this.recipeService.updateRecipe(this.convertToRecipeUpdateDto(this.recipe,this.route.snapshot.params['id']) ).subscribe({
          next: (detrecipe: DetailedRecipeDto) => {
            this.router.navigate(['/recipe/details/' + this.route.snapshot.params['id']]);
          },
          error: error => {
            this.defaultServiceErrorHandling(error);
          }
        }
      );
    } else if(this.mode === "fork"){
      this.recipeService.forkRecipe(this.convertToRecipeUpdateDto(this.recipe,this.route.snapshot.params['id']) ).subscribe({
          next: (detrecipe: DetailedRecipeDto) => {
            this.router.navigate(['/recipe/details/' + detrecipe.id]);
          },
          error: error => {
            this.defaultServiceErrorHandling(error);
          }
        }
      );
    }
  }

  convertToRecipeUpdateDto(recipe, id) {
    return {
      id: id,
      name: recipe.name,
      description: recipe.description,
      numberOfServings: recipe.numberOfServings,
      categories: recipe.categories,
      recipeSteps: recipe.recipeSteps,
      ingredients: recipe.ingredients
    };
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
      this.notification.error(this.errorMessage.toString(), 'Backend Fehler - Rezepte erstellen');
    } else {
      this.errorMessage = error.error;
      this.notification.error(this.errorMessage.toString(), 'Backend Fehler - Rezepte erstellen');
    }
  }

  vanishError() {
    this.error = false;
  }

  public ingredientChanged() :void{
    this.show.ingredients = this.possible.ingredients.filter(obj => obj.name.toLowerCase().includes(this.input.ingredient.toLowerCase()));
    console.log(this)
  }

  selectingredient(ingredient: IngredientDetailDto) : void{
    if(!this.recipe.ingredients.includes(ingredient)){
      this.recipe.ingredients.push(ingredient);
    }
  }
  deleteingredient(id: number):void{
    this.recipe.ingredients.splice(id,1)
  }

  deletecategory(id: number):void{
    this.recipe.categories.splice(id,1)
  }


  public categoryChanged() :void{
    this.show.category = this.possible.category.filter(obj => obj.name.toLowerCase().includes(this.input.category.toLowerCase()));
  }

  selectcategory(category: CategoryDetailDto) : void{
    if(!this.recipe.categories.includes(category)){
      this.recipe.categories.push(category);
    }
  }

  public addDescriptioneStep(){
    let x = new Step();
    x.whichstep = true;
    this.recipe.recipeSteps.push(x)
    this.input.recipestep.push('')
  }

  public addRecipeStep(){
    let x = new Step();
    x.whichstep = false;
    this.recipe.recipeSteps.push(x)
    this.input.recipestep.push('')
  }

  public recipestepChanged(i:number) :void{
    this.show.recipestep = this.possible.recipestep.filter(obj => obj.recipename.toLowerCase().includes(this.input.recipestep[i].toLowerCase()));
  }

  selectrecipestep(step: any, id: number) : void{
    this.input.recipestep[id] = step.recipename
    this.recipe.recipeSteps[id].recipeId = step.recipeId;
  }


  public stepup(i: number){
    let temp = this.recipe.recipeSteps[i].name;
    this.recipe.recipeSteps[i].name = this.recipe.recipeSteps[i-1].name ;
    this.recipe.recipeSteps[i-1].name  = temp ;

    temp = this.recipe.recipeSteps[i].description;
    this.recipe.recipeSteps[i].description = this.recipe.recipeSteps[i-1].description ;
    this.recipe.recipeSteps[i-1].description  = temp ;
  }

  public stepdown(i: number){
    let temp = this.recipe.recipeSteps[i].name;
    this.recipe.recipeSteps[i].name = this.recipe.recipeSteps[i+1].name ;
    this.recipe.recipeSteps[i+1].name  = temp ;

    temp = this.recipe.recipeSteps[i].description;
    this.recipe.recipeSteps[i].description = this.recipe.recipeSteps[i+1].description ;
    this.recipe.recipeSteps[i+1].description  = temp ;
  }

  public stepdel(i: number){
    this.recipe.recipeSteps.splice(i,1)
    this.input.recipestep.splice(i,1)
  }

}



