import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs/internal/Observable';
import { RecipeDetailDto } from 'src/app/dtos/recipe';
import { RecipeBookListDto } from 'src/app/dtos/recipe-book';
import { RecipeStepDetailDto, RecipeStepRecipeDetailDto } from 'src/app/dtos/recipe-step';
import { RecipeService } from 'src/app/services/recipe.service';
import { RecipeBookService } from 'src/app/services/recipebook.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.scss',
})
export class RecipeDetailComponent implements OnInit, OnDestroy{
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
  dummyRecipeBookSelectionModel: unknown;
  recipeSteps = [];
  returnClass = true;
  error = false;
  errorMessage = '';
  submitted = false;
  bannerError: string | null = null;
  currentRecipeBook: RecipeBookListDto;
  slideConfig = {
    "slidesToShow": 1,
    "slidesToScroll": 1,
    "dots": true,
    "arrows" : true,
    "infinite" : false
  }
  recipeForkedFrom = [];

  constructor(
    private service: RecipeService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private modalService: NgbModal,
    private recipeBookService: RecipeBookService,
    private titleService: Title,
  ) { }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      let observable = this.service.getRecipeDetailsBy(params['id']);
      observable.subscribe({
        next: data => {
          this.recipe = data;
          this.recipeSteps=this.recipe.recipeSteps;
          this.changeIngredientsToGramm();
          this.changeNutritionsToGramm();
          this.getForkedFromRecipeName();
          this.titleService.setTitle("Fork & Flavour | " + this.recipe.name);
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

  ngOnDestroy(): void {
    this.titleService.setTitle("Fork & Flavour");
  }

    isRecipeDescriptionStep(recipeStep: any): boolean {
    return recipeStep.hasOwnProperty('description') && !('recipe' in recipeStep);
  }

  isCategoriesNotEmpty(): boolean{
    return this.recipe.categories.length != 0;
  }
  isAllergensNotEmpty(): boolean{
    return this.recipe.allergens.length != 0;
  }

  isForkedFromRecipe(): boolean{
    if (this.recipe.id == this.recipe.forkedFromId) {
      return false;
    }
    return this.recipe.forkedFromId != 0 && this.recipe.forkedFromId != null;
  }

  getForkedFromRecipeName(): string[]{
    if (this.recipe.forkedFromId == 0 || this.recipe.forkedFromId == null) {
      return [];
    }
    this.service.getRecipeDetailsBy(this.recipe.forkedFromId).subscribe({
      next: data => {
        this.recipeForkedFrom.push(data.name);
      },
      error: error => {
        console.error('Error fetching Recipe', error);
        return [];
      }
    })
    console.log(this.recipeForkedFrom);
    return this.recipeForkedFrom;
  }

  openSpoonModal(spoonModal: TemplateRef<any>) {
    this.currentRecipeBook = null;
    this.modalService.open(spoonModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  spoon(form) {
    this.submitted = true;


    this.spoonRecipe(this.recipe.id,this.currentRecipeBook.id);
  }

  private spoonRecipe(recipeId:number, recipeBookId:number) {
    this.recipeBookService.spoonRecipe(recipeId,recipeBookId).subscribe({
          next: () => {
            this.notification.success(`Recipe added successfully.`);
            this.modalService.dismissAll();
            this.currentRecipeBook=null;
          },
          error: error => {
            this.notification.error(error);
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
      } else {
        this.errorMessage = error.error;
      }
    }

  recipeBookSuggestions = (input: string): Observable<RecipeBookListDto[]> =>
    this.recipeBookService.getRecipeBooksTheUserHasWriteAccessTo()

    public formatRecipeBook(recipeBook: RecipeBookListDto | null): string {
      return !recipeBook
        ? ""
        : `${recipeBook.name}`
    }

    public selectRecipeBook(recipeBook: RecipeBookListDto | null) {
      this.currentRecipeBook = recipeBook;
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


  toggleStep(step: any): void {
    if (step.recipe) {
      step.expanded = !step.expanded;
    }
  }

  editRecipe() {
    this.router.navigate(['recipe', 'edit', this.recipe.id]);
  }

  changeIngredientsToGramm() {
    for (let i = 0; i < this.recipe.ingredients.length; i++) {
      if (this.recipe.ingredients[i].amount >= 1000 && this.recipe.ingredients[i].unit === "mg") {
        this.recipe.ingredients[i].amount /= 1000;
        this.recipe.ingredients[i].unit = "g";
      }
    }
  }

  changeNutritionsToGramm() {
    for (let i = 0; i < this.recipe.nutritions.length; i++) {
      if (this.recipe.nutritions[i].value >= 1000 && this.recipe.nutritions[i].unit === "mg") {
        this.recipe.nutritions[i].value /= 1000;
        this.recipe.nutritions[i].unit = "g";
      }
    }
  }

}
