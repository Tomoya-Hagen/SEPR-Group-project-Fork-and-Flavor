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

  @Input()
  units: string[];

  @Output() deleteingredientCall = new EventEmitter();

  showunit: string[]


  @Output()
  ingredientChange: EventEmitter<IngredientDetailDto> = new EventEmitter<IngredientDetailDto>();

  constructor(
    private ingredientService: IngredientService
  ){}

  get Ingredient(): IngredientDetailDto {
    this.showunit = this.units;
    return this.ingredient;
  }

  selectunit(unit: string) : void{
    this.Ingredient.unit = unit
  }

  unitchanged() : void{
    this.showunit = this.units.filter(obj => obj.toLowerCase().includes(this.Ingredient.unit.toLowerCase()));
  }
  deleteingredient(): void{
    this.deleteingredientCall.emit(this.Ingredient.id);
  }
}
