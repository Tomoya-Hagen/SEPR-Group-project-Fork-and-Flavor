import { Component, OnInit} from '@angular/core';
import {Recipe} from "../../../dtos/Recipe";
import {NgForm} from "@angular/forms";
import {IngredientComponent} from "./ingredient/ingredient.component";
import {Ingredient} from "../../../dtos/Ingredient";

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
    ingredients: []
  }

  ingredientChangeHandler(updatedIngredient: Ingredient, index: number) {
    console.log(index);
    this.recipe.ingredients[index] = updatedIngredient;
    if(this.recipe.ingredients[this.recipe.ingredients.length-1].id != -1){
      this.recipe.ingredients.push({name: "", id: -1});
    }
  }

  ingredientchange() {
    this.recipe.ingredients.push({name: "", id: -1});
  }


  ngOnInit(): void {
    this.recipe.ingredients.push({name: "", id: -1});
  }

  public onSubmit(form: NgForm): void {

    console.log(this.recipe);
  }
}
