import { Component, OnInit } from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable, of} from 'rxjs';
import {RecipeBook} from '../../../dtos/recipe-book';
import { RecipeBookService } from '../../../services/recipe-book.service';
import { RecipeService } from 'src/app/services/recipe.service';
import { UserService } from 'src/app/services/user.service';
import {ToastrService} from 'ngx-toastr';

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
  recipeBook: RecipeBook = {
    id: 0,
    name: '',
    description: '',
    ownerId: 0,
    users: [],
    recipes: []
  };

  constructor(
    private recipeBookservice: RecipeBookService,
    private recipeService: RecipeService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    // private notification: ToastrService,
  ) { }
  ngOnInit(): void {
    var id = this.route.snapshot.params['id'];
    if (id) {
      this.recipeBookservice.getById(id).subscribe(recipeBook => {
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
        return 'Create Here A New Recipe Book';
      case RecipeBookCreateEditMode.edit:
        return 'Edit Recipe Book';
      default:
        return '?';
    }
  }

  public onSubmit(form: NgForm): void {
    console.log('is form valid?', form.valid, this.recipeBook);
    if (form.valid) {
      let observable: Observable<RecipeBook>;
      switch (this.mode) {
        case RecipeBookCreateEditMode.create:
          observable = this.recipeBookservice.createRecipeBook(this.recipeBook);
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
            console.error('Error creating recipe book', error);
            // this.notification.error('Could not create recipe book: ' + error.message);
          }
          if(this.mode === RecipeBookCreateEditMode.edit) {
            console.error('Error updating recipe book', error);
            // this.notification.error('Could not update recipe book: ' + error.message);
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


  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }

}
