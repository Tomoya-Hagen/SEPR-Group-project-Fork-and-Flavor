import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Ingredient} from "../../../../dtos/Ingredient";
import {of} from "rxjs";
import {IngredientService} from "../../../../services/ingredient.service";


@Component({
  selector: 'app-ingredient',
  templateUrl: './ingredient.component.html',
  styleUrl: './ingredient.component.scss'
})
export class IngredientComponent {

  @Input()
  ingredient: Ingredient;

  @Output()
  ingredientChange: EventEmitter<Ingredient> = new EventEmitter<Ingredient>();

  constructor(
    private ingredientService: IngredientService
  ){}

  get Ingredient(): Ingredient {
    return this.ingredient;
  }

  set Ingredient(value: Ingredient) {
    this.ingredient = value;
    this.ingredientChange.emit(this.ingredient)
  }


  public formatIngredient(ingredient: Ingredient | null): string {
    return ingredient?.name ?? '';
  }

  ingredientSuggestions = (input: string) => (input === '')
    ? of([])
    :  this.ingredientService.ingredientssByName(input, 5);

}
