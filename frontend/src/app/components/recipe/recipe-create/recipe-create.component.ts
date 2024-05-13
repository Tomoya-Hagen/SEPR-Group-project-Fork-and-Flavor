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

  ingredientchange() {
    console.log(this.recipe);
    this.recipe.ingredients.push({name: "", id: -1});
  }


  ngOnInit(): void {
    this.recipe.ingredients.push({name: "", id: -1});
  }

  public onSubmit(form: NgForm): void {

    console.log(this.recipe);
  }
}
