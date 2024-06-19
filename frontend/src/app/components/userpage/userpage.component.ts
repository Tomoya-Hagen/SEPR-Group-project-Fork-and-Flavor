import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {RecipeBookListDto} from "../../dtos/recipe-book";
import {userDto} from "../../dtos/user";
import {RecipeListDto} from "../../dtos/recipe";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";
import {Role} from "../../dtos/role";

@Component({
  selector: 'app-userpage',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink,
    NgIf
  ],
  templateUrl: './userpage.component.html',
  styleUrl: './userpage.component.scss'
})

export class UserpageComponent implements OnInit {

  recipeBook: RecipeBookListDto[];
  recipes: RecipeListDto[] = [];
  user: userDto;
  isMyPage: boolean = false;
  role: Role[] = [];

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
        },
        error: error => {
          console.error('Error fetching recipe books by user id', error);
          this.notification.error('Rezeptbücher für die Benutzerseite können nicht abgerufen werden.', "Backend Fehler - Benutzerseite Rezeptbücher");
        }
      });
      let observable2 = this.service.getUser(params['id']);
      observable2.subscribe({
        next: data => {
          this.user = data;
        },
        error: error => {
          console.error('Error fetching user by id', error);
          this.notification.error('Gesuchte Benutzerseite kann nicht geladen werden.', "Backend Fehler - Benutzerseite");
        }
      });
      let observable3 = this.service.getAllRecipesForUserId(params['id']);
      observable3.subscribe({
        next: data => {
          this.recipes = data;
          if (this.recipes.length > 0) {
            this.role.push(Role.cook);
          }
          if (this.recipes.length >= 10) {
            this.role.push(Role.starcook);
          }
        },
        error: error => {
          console.error('Error fetching recipes by user id', error);
          this.notification.error('Rezepte für die Benutzerseite können nicht abgerufen werden.', "Backend Fehler - Benutzerseite Rezepte");
        }
      });
      this.service.getCurrentUser().subscribe({
        next: (data: userDto) => {
          this.isMyPage = (data.id == params['id']);
        },
        error: (error: any) => {
          console.error('Error fetching current User', error);
          this.notification.error('Eigene Benutzerseite kann nicht geladen werden.', "Backend Fehler - Benutzerseite");
        }
      })
    });
  }
}
