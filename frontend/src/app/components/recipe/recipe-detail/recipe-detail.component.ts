import {Component, HostListener, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs/internal/Observable';
import {Recipe, RecipeDetailDto} from 'src/app/dtos/recipe';
import { RecipeBookListDto } from 'src/app/dtos/recipe-book';
import { RecipeStepDetailDto, RecipeStepRecipeDetailDto } from 'src/app/dtos/recipe-step';
import { RecipeService } from 'src/app/services/recipe.service';
import { RecipeBookService } from 'src/app/services/recipebook.service';
import { Title } from '@angular/platform-browser';
import { UserService } from 'src/app/services/user.service';
import {RecipeModalComponent} from "./recipe-modal/recipe-modal.component";
import { RatingCreateDto, RatingListDto } from 'src/app/dtos/rating';
import { RatingService } from 'src/app/services/rating.service';
import { NgForm } from '@angular/forms';
import {AuthService} from "../../../services/auth.service";
import {tap} from "rxjs/operators";
import {catchError, of} from "rxjs";
import {Role} from "../../../dtos/role";
import {userDto} from "../../../dtos/user";

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.scss',
})
export class RecipeDetailComponent implements OnInit, OnDestroy {
  @ViewChild('spoonRecipeModal', {static: true}) spoonRecipeModal: TemplateRef<any>;
  @ViewChild('showForkedRecipesModal', { static: true }) showForkedRecipesModal: TemplateRef<any>;

  recipe: RecipeDetailDto = {
    id: 0,
    rating: 0,
    name: "",
    description: "",
    numberOfServings: 0,
    forkedFrom: null,
    ownerId: 0,
    categories: [],
    isDraft: false,
    recipeSteps: [],
    ingredients: [],
    allergens: [],
    nutritions: [],
    forkedRecipes: [],
    isVerified: false,
    verifications: 0,
  };

  ratings: RatingListDto[] = [];
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
    "arrows": true,
    "infinite": false
  }
  recipeForkedFrom: string;
  showNutrition: boolean = false;
  screenWidth: number;
  isOwner: boolean = false;
  isVerified: boolean = false;
  isStarcook: boolean = false;
  role: Role[] = [];
  areRatingsLoaded: boolean = false;
  rating: RatingCreateDto = {
    review: "",
    taste: 0,
    easeOfPrep: 0,
    recipeId: 0,
    cost: 0
  }
  selectedPortions: number;
  originalServings: number;
  hasForkedRecipes: boolean = false;
  originalNutritions = [];
  originalIngredients = [];
  recipes: Recipe[] = [];
  totalElements: number;
  page: number = 1;
  size: number = 3;
  loggedIn: boolean = false;
  hasRated: boolean = false;
  menuOptions = [];
  currentUser: userDto = null;
  showForkedRecipesModalModal: boolean = false;

  constructor(
    private ratingService: RatingService,
    private service: RecipeService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private modalService: NgbModal,
    private recipeBookService: RecipeBookService,
    private titleService: Title,
    private userService: UserService,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.screenWidth = window.innerWidth;
    this.route.params.subscribe(params => {
      let observable = this.service.getRecipeDetailsBy(params['id']);
      observable.subscribe({
        next: data => {
          this.recipe = data;
          this.rating.recipeId = this.recipe.id;
          this.recipeSteps = this.recipe.recipeSteps;
          this.originalNutritions = this.recipe.nutritions;
          this.originalIngredients = this.recipe.ingredients;
          this.originalServings = this.recipe.numberOfServings;
          this.selectedPortions = this.recipe.numberOfServings;
          this.changeIngredientsToGramm();
          this.changeNutritionsToGramm();
          this.isCurrentUserOwner();
          this.orderNutritions();
          if (this.loggedIn) {
            this.getBadgesUser();
            this.hasVerified();
          }

          if (this.recipe.forkedRecipes.length > 0) {
            this.hasForkedRecipes = true;
          }
          this.titleService.setTitle("Fork & Flavour | " + this.recipe.name);
          this.onPageChange(1);
        },
        error: error => {
          this.router.navigate(['not-found']);
        }
      });
    });
    this.authService.isLogged()
      .pipe(
        tap((isLoggedIn: boolean) => {
          this.loggedIn = true;
        })
      )
      .subscribe();
  }

  getBadgesUser() {
    let observable4 = this.userService.getBadgesOfCurrentUser();
    observable4.subscribe({
      next: data => {
        this.role = data;
        for (let i = 0; i < this.role.length; i++) {
          if (this.role[i].length === Role.starcook.length) {
            this.isStarcook = true;
          }
        }
      },
      error: error => {
        console.error('Error fetching badges by user id', error);
      }
    });
  }

  hasVerified() {
    this.service.getHasVerified(this.recipe.id).subscribe({
      next: data => {
        this.isVerified = data;
        this.updateMenuOptions();
      },
      error: error => {
        console.error('Error fetching badges by user id', error);
    }});
  }

  ngOnDestroy(): void {
    this.titleService.setTitle("Fork & Flavour");
  }

  getCategoryList(): string {
    return this.recipe.categories.map(category => category.name).join(', ');
  }

  updateMenuOptions() {
    if (this.loggedIn) {
      this.menuOptions = [
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
          label: 'Rezept forken',
          action: () => this.fork()
        },
        {
          label: 'Rezept spoonen', buttonClass: 'info-box-3',
          iconClass: 'info-box-3',
          action: () => this.openSpoonModal(this.spoonRecipeModal)
        },
        {
          label: 'Rezept verifizieren',
          action: () => this.addVerfication(),
          disabled: this.isOwner || this.isVerified
        },
        {
          label: 'Rezepte die gut dazupassen bearbeiten',
          action: () => this.openRecipeGoesWellWithModal(),
          disabled: !this.isOwner
        }
      ];
    }
  }

  roundTo(value: number): number {
    const factor = Math.pow(10, 1);
    return Math.round(value * factor) / factor;
  }

  increment() {
    if (this.selectedPortions < 10) {
      this.selectedPortions++;
      this.onPortionsChange();
    }
  }

  decrement() {
    if (this.selectedPortions > 1) {
      this.selectedPortions--;
      this.onPortionsChange();
    }
  }

  onPortionsChange(): void {
    this.adjustIngredientsAndNutritions();
    this.changeIngredientsToGramm();
    this.changeNutritionsToGramm();
  }

  adjustIngredientsAndNutritions(): void {
    const ratio = this.selectedPortions / this.originalServings;
    this.recipe.ingredients = this.originalIngredients.map(ingredient => ({
      ...ingredient,
      amount: this.roundTo(ingredient.amount * ratio)
    }));
    this.recipe.nutritions = this.originalNutritions.map(nutrition => ({
      ...nutrition,
      value: this.roundTo(nutrition.value * ratio)
    }));
  }

  orderNutritions(): void {
    this.recipe.nutritions.sort((a, b) => a.id - b.id);
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

  openSpoonModal(spoonModal: TemplateRef<any>) {
    this.currentRecipeBook = null;
    this.modalService.open(spoonModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  openForkedRecipesModal() {
    this.showForkedRecipesModalModal = true;
    this.modalService.open(this.showForkedRecipesModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  closeForkedRecipes() {
    this.showForkedRecipesModalModal = false;
    this.modalService.dismissAll();
  }

  fork() {
    this.router.navigate(["/recipe/fork/" + this.recipe.id])
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
        this.recipe.ingredients[i].amount = this.roundTo(this.recipe.ingredients[i].amount);
        this.recipe.ingredients[i].unit = "g";
      }
    }
  }

  changeNutritionsToGramm() {
    for (let i = 0; i < this.recipe.nutritions.length; i++) {
      if (this.recipe.nutritions[i].value >= 1000 && this.recipe.nutritions[i].unit === "mg") {
        this.recipe.nutritions[i].value /= 1000;
        this.recipe.nutritions[i].value = this.roundTo(this.recipe.nutritions[i].value);
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
      this.currentUser = currentUser;
      if (currentUser && this.recipe.ownerId === currentUser.id) {
        this.isOwner = true;
      }
      this.updateMenuOptions();
    });
  }

  loadRatings(){
    this.ratingService.getRatingsByRecipeId(this.recipe.id).subscribe({
      next: data => {
        this.notification.success(`Ratings loaded successfully.`);
        this.ratings = data;
        this.areRatingsLoaded = true;
        // @ts-ignore
        this.hasRated = this.ratings.some(rating => rating.user.name === localStorage.getItem("username"));
        console.log(this.hasRated);
      },
      error: error => {
        this.notification.error(error);
        this.defaultServiceErrorHandling(error);
      }
    }
  );
  }

  onSubmitRating(form:NgForm){
    this.ratingService.createRating(this.rating).subscribe({
      next: () => {
        this.notification.success(`Rating added successfully.`);
        this.modalService.dismissAll();
        this.currentRecipeBook=null;
        this.loadRatings();
        this.hasRated = true;
      },
      error: error => {
        this.notification.error(error);
        this.defaultServiceErrorHandling(error);
      }
    }
  );
  }

  openRatingModal(modal: TemplateRef<any>) {
    if (this.loggedIn){
      this.modalService.open(modal, {ariaLabelledBy: 'modal-basic-title'});
    } else {
      this.notification.error('Sie müssen eingeloggt sein, um ein Rating abzugeben.', 'Nicht eingeloggt');
      this.router.navigate(['/login']);
    }
  }

  closeRatingModal() {
    this.modalService.dismissAll();
  }

  isFormValid():boolean{
    return this.rating.cost != 0 &&
    this.rating.easeOfPrep != 0 &&
    this.rating.taste != 0 &&
    this.rating.review.length > 0
  }

  public selectEaseOfPrep(value: number | null) {
    this.rating.easeOfPrep = value;
  }

  public selectCost(value: number | null) {
    this.rating.cost = value;
  }

  public selectTaste(value: number | null) {
    this.rating.taste = value;
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

  goToRecipe(id: number) {
    this.router.navigate(['/recipe/details', id]);
  }

  addVerfication() {
     this.service.verifyRecipe(this.recipe.id).subscribe({
        next: () => {
          this.notification.success(`Rezept verifiziert.`);
          this.isVerified = true;
          this.updateMenuOptions();
          this.recipe.verifications++;
        },
        error: error => {
          this.notification.error(error);
          this.defaultServiceErrorHandling(error);
        }
    });
   }
}
