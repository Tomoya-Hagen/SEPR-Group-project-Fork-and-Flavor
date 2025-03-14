import { Component, OnInit } from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {catchError, Observable, of} from 'rxjs';
import { RecipeBookCreateDto} from '../../../dtos/recipe-book';
import { RecipeBookService } from '../../../services/recipebook.service';
import { RecipeService } from 'src/app/services/recipe.service';
import { UserService } from 'src/app/services/user.service';
import { RecipeListDto } from 'src/app/dtos/recipe';
import {userListDto} from "../../../dtos/user";
import {ToastrService} from "ngx-toastr";
import {switchMap, tap} from "rxjs/operators";
import {AuthService} from "../../../services/auth.service";

export enum RecipeBookCreateEditMode {
  create,
  edit,
}
@Component({
  selector: 'app-recipebook-create-edit',
  templateUrl: './recipebook-create-edit.component.html',
  styleUrl: './recipebook-create-edit.component.scss'
})
export class RecipebookCreateEditComponent implements OnInit {

  mode: RecipeBookCreateEditMode = RecipeBookCreateEditMode.create;
  recipeBook: RecipeBookCreateDto = {
    name: '',
    description: '',
    users: null,
    recipes: null
  };
  users: (userListDto | null)[] = [];
  recipes: (RecipeListDto | null)[] = [];
  currentUserId: number = 0;
  dummyUserSelectionModel: unknown;
  dummyRecipeSelectionModel: unknown;
  isOwner: boolean = false;
  currentUser: (userListDto | null);
  canEdit: boolean = true;

  constructor(
    private recipeBookService: RecipeBookService,
    private recipeService: RecipeService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private authService: AuthService,
  ) { }
  ngOnInit(): void {
    this.authService.isLogged()
    this.userService.getCurrentUser().pipe(
      tap((user: userListDto) => {
        this.currentUserId = user.id;
        this.currentUser = user;
        this.isOwnerOfRecipeBook();
      }),
      switchMap(() => this.authService.isLogged()),
      tap((isLoggedIn: boolean) => {
        console.log('Is logged in:', isLoggedIn);
      }),
      catchError((error) => {
      console.error('Error:', error);
        this.notification.error('Sie müssen sich als Benutzer anmelden oder als Benutzer registrieren, um ein Rezeptbuch zu erstellen.' , 'Rezeptbuch kann nicht erstellt werden.');
        this.router.navigate(['/login']);
      return of(false);
    })
      )
      .subscribe();
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.recipeBookService.getById(id).subscribe(recipeBook => {
        this.recipeBook = recipeBook;
        this.recipes = recipeBook.recipes;
        this.users = recipeBook.users;
        this.mode = RecipeBookCreateEditMode.edit;
        if (!this.canEdit) {
          for (let i = 0; i < this.users.length; i++) {
            if (this.currentUserId == this.users[i].id) {
              this.canEdit = true;
            }
          }
          if (!this.canEdit) {
            this.notification.error('Sie können nicht Rezeptbücher bearbeiten, wo Sie nicht beteiligt sind.', 'Rezeptbuch kann nicht bearbeitet werden.');
            this.router.navigate(['/recipebook']);
          }
        }
      });

    } else {
      this.mode = RecipeBookCreateEditMode.create;
      this.isOwner = true;
    }
  }

  isOwnerOfRecipeBook() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.recipeBookService.getUserIdByRecipeBookId(id).subscribe({
        next: data => {
          this.isOwner = (data == this.currentUserId);
          this.canEdit = (data == this.currentUserId);
        }, error: error => {
          console.error('Error Fehler beim Laden der User id vom Rezeptbuch', error);
          this.notification.error('Fehler beim Laden der User id vom Rezeptbuch.',"Fehler - User Id");
        }
      });
    }
  }

  public get heading(): string {
    switch (this.mode) {
      case RecipeBookCreateEditMode.create:
        return 'Neues Rezeptbuch erstellen';
      case RecipeBookCreateEditMode.edit:
        return 'Rezeptbuch bearbeiten';
      default:
        return '?';
    }
  }

  public onSubmit(form: NgForm): void {
    this.isFormValid();
    console.log('is form valid?', form.valid, this.recipeBook);
    if (form.valid && this.isFormValid()) {
      let observable: Observable<any>;
      switch (this.mode) {
        case RecipeBookCreateEditMode.create:
          this.recipeBook.recipes = this.recipes;
          this.recipeBook.users = this.users;
          observable = this.recipeBookService.createRecipeBook(this.recipeBook);
          break;
        case RecipeBookCreateEditMode.edit:
          observable = this.recipeBookService.update(this.recipeBook, this.route.snapshot.params['id']);
          break;
        default:
          console.error('Unknown RecipeBookCreateEditMode', this.mode);
          return;
      }
      observable.subscribe({
        next: data => {
          this.notification.success(`Rezeptbuch ${this.recipeBook.name} erfolgreich ${this.modeActionFinished}.`);
          this.router.navigate(['/recipebook']);
        },
        error: error => {
          if(this.mode === RecipeBookCreateEditMode.create) {
            console.error('Error creating recipe book', error);
            this.notification.error('Rezeptbuch kann nicht erstellt werden.', 'Fehler - Rezeptbuch erstellen');
          }
          if(this.mode === RecipeBookCreateEditMode.edit) {
            console.error('Error editing recipe book', error);
            this.notification.error('Rezeptbuch kann nicht bearbeitet werden.', 'Fehler - Rezeptbuch bearbeiten');
          }
        }
      });
    }
  }

  private get modeActionFinished(): string {
    switch (this.mode) {
      case RecipeBookCreateEditMode.create:
        return 'erstellt';
      case RecipeBookCreateEditMode.edit:
        return 'bearbeitet';
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case RecipeBookCreateEditMode.create:
        return 'Erstellen';
      case RecipeBookCreateEditMode.edit:
        return 'Bearbeiten';
      default:
        return '?';
    }
  }

  recipeSuggestions = (input: string) => (input === '')
  ? of([])
  :  this.recipeService.recipesByName(input, 0,5);

  userSuggestions = (input: string) => (input === '')
  ? of([])
  :  this.userService.usersByName(input, 5, this.currentUserId);

  public formatRecipeName(recipe: RecipeListDto | null): string {
    return recipe?.name ?? '';
  }

  public formatUserName(user: userListDto | null): string {
    return user?.name ?? '';
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public isFormValid(): boolean {
    let isValid = true;
    if (this.isOwner && this.recipes.length <= 0) {
      this.notification.error("Rezepte können nicht leer sein")
      isValid = false;
    }

    if (this.isOwner && this.recipeBook.description == '') {
      this.notification.error("Die Beschreibung kann nicht leer sein")
      isValid = false;
    }

    if (this.isOwner && this.recipeBook.name == '') {
      this.notification.error("Der Name kann nicht leer sein")
      isValid = false;
    }

    return isValid;

  }

  public addRecipe(recipe: RecipeListDto | null) {
    if (!recipe) return;
    // This should happen late, when the ngModelChange hook has completed,
    // so that changing dummyRecipeSelectionModel works
    setTimeout(() => {
      const recipes = this.recipes;
      for (let i = 0; i < this.recipes.length + 1; i++) {
        if (recipes[i]?.id === recipe.id) {
          // this.notification.error(`${recipe.name} is already in participant list`, "Duplicate Participant");
          this.dummyRecipeSelectionModel = null;
          return;
        }
        if (recipes[i] == null) {
          recipes[i] = recipe;
          this.dummyRecipeSelectionModel = null;
          return;
        }
      }
    });
  }

  public addUser(user: userListDto | null) {
    if (!user) return;
    setTimeout(() => {
      const users = this.users;
      for (let i = 0; i < this.users.length + 1; i++) {
        if (users[i]?.id === user.id) {
          // this.notification.error(`${recipe.name} is already in participant list`, "Duplicate Participant");
          this.dummyUserSelectionModel = null;
          return;
        }
        if (users[i] == null) {
          users[i] = user;
          this.dummyUserSelectionModel = null;
          return;
        }
      }
    });
  }

  public removeRecipe(index: number) {
    this.recipes.splice(index, 1);
  }

  public removeUser(index: number) {
    this.users.splice(index, 1);
  }

  public removeContributor() {
    for (let i = 0; i < this.users.length; i++) {
      if(this.users[i].id == this.currentUserId) {
        this.users.splice(i,1);
        break;
      }
    }
    this.recipeBookService.update(this.recipeBook, this.route.snapshot.params['id']).subscribe({
      next: data => {
        this.notification.success(`Rezeptbuch ${this.recipeBook.name} erfolgreich verlassen.`);
        this.router.navigate(['/recipebook']);
      },
      error: error => {
        console.error('Error leaving recipe book', error);
        this.notification.error('Rezeptbuch kann nicht verlassen werden.', 'Fehler - Rezeptbuch verlassen');
      }
    });

  }
}
