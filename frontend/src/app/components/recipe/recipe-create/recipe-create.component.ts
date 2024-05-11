import { Component, OnInit} from '@angular/core';
import {AppModule} from "../../../app.module";
import {Ingredient} from "../../../dtos/Ingredient";
import {Recipe} from "../../../dtos/Recipe";
import {FormsModule, NgForm, NgModel} from "@angular/forms";
import {of} from "rxjs";
import {IngredientService} from "../../../services/ingredient.service";

@Component({
  selector: 'app-recipe-create',
  standalone: true,
  imports: [
    AppModule,
    FormsModule
  ],
  templateUrl: './recipe-create.component.html',
  styleUrl: './recipe-create.component.scss'
})
export class RecipeCreateComponent implements OnInit{

  recipe: Recipe = {
    name: '',
    description: '',
    servings: 0,
    ownerid: -1,
    ingredients: null
  }
  ingredient: Ingredient = {
    name: '',
    id: 0
  };

  constructor(
    private ingredientService: IngredientService
  ){}


  public formatIngredient(ingredient: Ingredient | null): string {
    return ingredient?.name ?? '';
  }

  ingredientSuggestions = (input: string) => (input === '')
    ? of([])
    :  this.ingredientService.ingredientssByName(input, 5);


  ngOnInit(): void {

  }

  public onSubmit(form: NgForm): void {

  }
}
