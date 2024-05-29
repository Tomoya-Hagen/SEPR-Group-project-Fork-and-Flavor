import {Component, EventEmitter, Input, Output} from '@angular/core';
import {IngredientDetailDto} from "../../../../dtos/ingredient";
import {of} from "rxjs";
import {IngredientService} from "../../../../services/ingredient.service";


@Component({
  selector: 'app-ingredient',
  templateUrl: './ingredient.component.html',
  styleUrl: './ingredient.component.scss'
})
export class IngredientComponent {

  @Input()
  ingredient: IngredientDetailDto;

  @Output()
  ingredientChange: EventEmitter<IngredientDetailDto> = new EventEmitter<IngredientDetailDto>();

  constructor(
    private ingredientService: IngredientService
  ){}

  get Ingredient(): IngredientDetailDto {
    return this.ingredient;
  }

  set Ingredient(value: IngredientDetailDto) {
    this.ingredient = value;
    this.ingredientChange.emit(this.ingredient)
  }


  public formatIngredient(ingredient: IngredientDetailDto | null): string {
    return ingredient?.name ?? '';
  }

  ingredientSuggestions = (input: string) => (input === '')
    ? of([])
    :  this.ingredientService.ingredientssByName(input, 5);


  public ingredientchanged():void{
    if(this.Ingredient == null){
      this.Ingredient = {id: 0, unit: "", name: "", amount: null};
    }
  }
}
