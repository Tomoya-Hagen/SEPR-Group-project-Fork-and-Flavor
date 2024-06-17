import {Component, HostListener, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs/internal/Observable';
import {Recipe, RecipeDetailDto, RecipeListDto} from 'src/app/dtos/recipe';
import { RecipeBookListDto } from 'src/app/dtos/recipe-book';
import { RecipeStepDetailDto, RecipeStepRecipeDetailDto } from 'src/app/dtos/recipe-step';
import { RecipeService } from 'src/app/services/recipe.service';
import { RecipeBookService } from 'src/app/services/recipebook.service';
import { Title } from '@angular/platform-browser';
import { UserService } from 'src/app/services/user.service';
import {RecipeModalComponent} from "./recipe-modal/recipe-modal.component";

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.scss',
})
export class RecipeDetailComponent implements OnInit, OnDestroy{
  @ViewChild('spoonRecipeModal', { static: true }) spoonRecipeModal: TemplateRef<any>;

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
  showNutrition: boolean = false;
  screenWidth: number;
  isOwner: boolean = false;
  recipes: Recipe[] = [];
  totalElements: number;
  page: number = 1;
  size: number = 3;
  menuOptions = [
    {
      label: 'Neues Rezept erstellen',
      action: () => this.addRecipe()
    },
    {
      label: 'Rezept bearbeiten',
      action: () => this.editRecipe(),
      disabled: !this.isOwner
    },
    {
      label: 'Rezept spoonen', buttonClass: 'info-box-3',
      iconClass: 'info-box-3', // Use an appropriate icon class
      action: () => this.openSpoonModal(this.spoonRecipeModal)
    },
    {
      label: 'Rezepte die gut dazupassen bearbeiten',
      action: () => this.openRecipeGoesWellWithModal(),
      disabled: !this.isOwner
    }
  ];

  constructor(
    private service: RecipeService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private modalService: NgbModal,
    private recipeBookService: RecipeBookService,
    private titleService: Title,
    private userService: UserService,
  ) { }

  ngOnInit(): void {
    this.screenWidth = window.innerWidth;
    this.route.params.subscribe(params => {
      let observable = this.service.getRecipeDetailsBy(params['id']);
      observable.subscribe({
        next: data => {
          this.recipe = data;
          this.recipeSteps = this.recipe.recipeSteps;
          this.changeIngredientsToGramm();
          this.changeNutritionsToGramm();
          this.getForkedFromRecipeName();
          this.isCurrentUserOwner();
          this.titleService.setTitle("Fork & Flavour | " + this.recipe.name);
          this.onPageChange(1);
        },
        error: error => {
          console.error('Error fetching recipe', error);
          this.notification.error('Rezepte können nicht abgerufen werden.', "Backend Fehler - Rezepte");
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
        console.error('Error forking recipe.', error);
        const errorMessage = error.message.message;
        this.notification.error('Fork Rezepte ist nicht möglich.' + errorMessage, "Backend Fehler - Rezepte");
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
            this.notification.success(`Rezepte hinzufügen war erfolgreich.`, "Rezepte erstellen erfolgreich!");
            this.modalService.dismissAll();
            this.currentRecipeBook=null;
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
      this.notification.error('Spoon Rezepte ist nicht möglich.' + this.errorMessage, 'Backend Fehler - Rezepte');
    } else {
      this.errorMessage = error.error;
      this.notification.error( 'Spoon Rezepte ist nicht möglich.' + this.errorMessage, 'Backend Fehler - Rezepte');
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

  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    this.screenWidth = window.innerWidth;
  }

  toggleNutrition() {
    this.showNutrition = !this.showNutrition;
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

  navigateToDetailsInNewTab(index: number) {
    const baseUrl = window.location.origin;
  const url = `${baseUrl}/#/recipe/details/${index}`;
    window.open(url, '_blank');
  }

  isCurrentUserOwner() {
    this.userService.getCurrentUser().subscribe(currentUser => {
      if (currentUser && this.recipe.ownerId === currentUser.id) {
        this.isOwner = true;
      }
    });
  }

  async getRecipesGoingWellTogether(): Promise<void> {
    const data = this.service.getGoesWellWith(this.recipe.id, this.page - 1, this.size)
      .subscribe( {
        next: (data: any) : void => {
          this.recipes = data.content;
          this.totalElements = data.totalElements;
        },
        error: error => {
          console.error('Error fetching recipes.', error);
          this.notification.error('Rezepte können nicht abgerufen werden.', 'Backend Fehler - Rezepte');
        }
    })
  }

  onPageChange(pageNumber: number): void {
    this.page = pageNumber;
    this.getRecipesGoingWellTogether().then(r => {});
  }

  detail(id: number): void {
    this.router.navigate(['/recipe/details', id]);
  }

  openRecipeGoesWellWithModal() {
    const modalRef = this.modalService.open(RecipeModalComponent);
    modalRef.componentInstance.recipeId = this.recipe.id;

    modalRef.componentInstance.updateRecipes.subscribe((updatedRecipes: Recipe[]) => {
      this.recipes = updatedRecipes;
      this.service.updateGoWellWithRecipes(this.recipe.id, this.recipes).subscribe(
        () => {
          this.getRecipesGoingWellTogether();
        },
        error => {
          console.error('Error updating recipes.', error);
          this.notification.error('Rezepte können nicht aktualisiert werden.', 'Backend Fehler - Rezepte');
        }
      );
    });
  }

  addRecipe() {
    this.router.navigate(['recipe/create']);
  }

}
