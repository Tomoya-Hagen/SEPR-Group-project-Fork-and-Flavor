import { Component, OnInit } from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {catchError, Observable, of} from 'rxjs';
import {RecipeBook, RecipeBookCreateDto} from '../../../dtos/recipe-book';
import { RecipeBookService } from '../../../services/recipebook.service';
import { RecipeService } from 'src/app/services/recipe.service';
import { UserService } from 'src/app/services/user.service';
import { RecipeListDto } from 'src/app/dtos/recipe';
import {userListDto} from "../../../dtos/user";
import {ToastrService} from "ngx-toastr";
import {tap} from "rxjs/operators";
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
  bannerError: string | null = null;
  users: (userListDto | null)[] = [];
  recipes: (RecipeListDto | null)[] = [];
  dummyUserSelectionModel: unknown;
  dummyRecipeSelectionModel: unknown;

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
      .pipe(
        tap((isLoggedIn: boolean) => {
          console.log('Is logged in:', isLoggedIn);
        }),
        catchError((error) => {
          console.error('Error:', error);
          this.notification.error('You have to login as user to create recipebook.' , 'Backend Error - Recipebook');
          this.router.navigate(['/login']);
          return of(false); // Handle the error and return a fallback value
        })
      )
      .subscribe();
    var id = this.route.snapshot.params['id'];
    if (id) {
      this.recipeBookService.getById(id).subscribe(recipeBook => {
        this.recipeBook = recipeBook;
        this.mode = RecipeBookCreateEditMode.edit;
      });
    } else {
      this.mode = RecipeBookCreateEditMode.create;
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
    console.log('is form valid?', form.valid, this.recipeBook);
    if (form.valid && this.isFormValid()) {
      let observable: Observable<RecipeBook>;
      switch (this.mode) {
        case RecipeBookCreateEditMode.create:
          this.recipeBook.recipes = this.recipes;
          this.recipeBook.users = this.users;
          observable = this.recipeBookService.createRecipeBook(this.recipeBook);
          break;
        case RecipeBookCreateEditMode.edit:
          // observable = this.recipeBookservice.update(this.recipeBook);
          break;
        default:
          console.error('Unknown RecipeBookCreateEditMode', this.mode);
          return;
      }
      observable.subscribe({
        next: data => {
          // this.notification.success(`Recipe book ${this.recipeBook.name} successfully ${this.modeActionFinished}.`);
          this.router.navigate(['/recipebook']);
        },
        error: error => {
          if(this.mode === RecipeBookCreateEditMode.create) {
            console.error('Error creating recipebook', error);
            this.notification.error('Could not create recipebook.', 'Backend Error - Recipebook Create');
          }
          if(this.mode === RecipeBookCreateEditMode.edit) {
            console.error('Error editing recipebook', error);
            this.notification.error('Could not edit recipebook.', 'Backend Error - Recipebook Edit');
          }
        }
      });
    }
  }

  private get modeActionFinished(): string {
    switch (this.mode) {
      case RecipeBookCreateEditMode.create:
        return 'created';
      case RecipeBookCreateEditMode.edit:
        return 'updated';
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case RecipeBookCreateEditMode.create:
        return 'Create';
      case RecipeBookCreateEditMode.edit:
        return 'Update';
      default:
        return '?';
    }
  }

  recipeSuggestions = (input: string) => (input === '')
  ? of([])
  :  this.recipeService.recipesByName(input, 5);

  userSuggestions = (input: string) => (input === '')
  ? of([])
  :  this.userService.usersByName(input, 5);

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
    return this.recipes.length > 0 && this.recipeBook.description !== '' && this.recipeBook.name !== '';
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

}
