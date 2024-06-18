import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {RecipeBookListDto} from "../../dtos/recipe-book";
import {userDto} from "../../dtos/user";
import {RecipeListDto} from "../../dtos/recipe";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-userpage',
  templateUrl: './userpage.component.html',
  styleUrl: './userpage.component.scss'
})
export class UserpageComponent implements OnInit {
  recipeBook: RecipeBookListDto[];
  recipes: RecipeListDto[] = [];
  user: userDto;
  isMyPage: boolean = false;

  constructor(
    private service: UserService,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let observable = this.service.getAllRecipeBooksForUserId(params['id']);
      observable.subscribe({
        next: data => {
          this.recipeBook = data;
          this.loadRecipes(params['id']);
          this.loadUser(params['id']);
          this.loadCurrentUser(params['id']);

        },
        error: error => {
          console.error('Error fetching recipe books by user id', error);
          this.notification.error('Rezeptbücher für die Benutzerseite können nicht abgerufen werden.',"Backend Fehler - Benutzerseite Rezeptbücher");
        }
      });

    });
  }

  loadRecipes(params: any) {
    this.service.getAllRecipesForUserId(params).subscribe({
      next: data => {
        this.recipes = data;
      },
      error: error => {
        console.error('Error fetching recipes by user id', error);
        this.notification.error('Rezepte für die Benutzerseite können nicht abgerufen werden.',"Backend Fehler - Benutzerseite Rezepte");
      }
    });
  }

  loadUser(params: any) {
    this.service.getUser(params).subscribe({
      next: data => {
        this.user = data;
      },
      error: error => {
        console.error('Error fetching user by id', error);
        this.notification.error('Gesuchte Benutzerseite kann nicht geladen werden.',"Backend Fehler - Benutzerseite");
      }
    });
  }

  loadCurrentUser(params: any) {
    this.service.getCurrentUser().subscribe({
      next: (data: userDto) => {
        this.isMyPage = (data.id == params);
      },
      error: (error: any) => {
        console.error('Error fetching current User', error);
        this.notification.error('Eigene Benutzerseite kann nicht geladen werden.',"Backend Fehler - Benutzerseite");
      }
    })
  }
}
