import {Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RecipeDetailDto } from 'src/app/dtos/recipe';
import { RecipeStepDetailDto, RecipeStepRecipeDetailDto } from 'src/app/dtos/recipe-step';
import { RecipeService } from 'src/app/services/recipe.service';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.scss',
})
export class RecipeDetailComponent implements OnInit{
  recipe: RecipeDetailDto = {
    id: 0,
    rating: 0,
    name: "",
    description: "",
    numberOfServings: 0,
    forkedFromId: 0,
    ownerId: 0,
    categories: [],
    isDraft: false,
    recipeSteps: [],
    ingredients: [],
    allergens: [],
    nutritions: []
  };
  recipeSteps = [];
  returnClass = true;
  bannerError: string | null = null;
  slideConfig = {
    "slidesToShow": 1,
    "slidesToScroll": 1,
    "dots": true,
    "arrows" : true,
    "infinite" : false
  }

  constructor(
    private service: RecipeService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) { }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      let observable = this.service.getRecipeDetailsBy(params['id']);
      observable.subscribe({
        next: data => {
          this.recipe = data;
          this.recipeSteps=this.recipe.recipeSteps;
        },
        error: error => {
          console.error('Error fetching Recipe', error);
          this.bannerError = 'Could not fetch recipe: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Recipe');
          this.router.navigate([''])
        }
      });
    });
  }

  isRecipeDescriptionStep(recipeStep: RecipeStepDetailDto) :boolean{
    if(recipeStep.hasOwnProperty('description')) {
        return true;
    } else {
        return false;
    }
  }

  isCategoriesNotEmpty(): boolean{
    return this.recipe.categories.length != 0;
  }
  isAllergensNotEmpty(): boolean{
    return this.recipe.allergens.length != 0;
  }

  addRecipeStepsfromRecipe(recipeStep:RecipeStepRecipeDetailDto){
    let recipeSteps = [];
    for (let i = 0; i < this.recipeSteps.length; i++) {
      if (this.recipeSteps[i]===recipeStep) {
        for (let j = 0; j < recipeStep.recipe.recipeSteps.length; j++) {
          recipeSteps.push(recipeStep.recipe.recipeSteps[j]);
        } 
      } else{
        let newRecipeStep = JSON.parse(JSON.stringify(this.recipeSteps[i]));
        recipeSteps.push(newRecipeStep)
      }
    }
    this.recipeSteps=recipeSteps;
  }
}
