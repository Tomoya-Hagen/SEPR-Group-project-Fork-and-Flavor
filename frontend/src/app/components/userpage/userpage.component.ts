import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {RecipeBookListDto} from "../../dtos/recipe-book";
import {userDto} from "../../dtos/user";
import {RecipeListDto} from "../../dtos/recipe";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";
import {RatingListDto} from "../../dtos/rating";
import {RatingService} from "../../services/rating.service";

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


  recipeBook: RecipeBookListDto[] = [];
  recipes: RecipeListDto[] = [];
  user: userDto = {
    id: 0,
    name: ""
  };
  isMyPage: boolean = false;
  ratings: RatingListDto[] = [];

  constructor(
    private service: UserService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private ratingService: RatingService,
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
          this.notification.error('Rezeptbücher für die Benutzerseite können nicht abgerufen werden.',"Fehler - Benutzerseite Rezeptbücher");
        }
      });
      let observable2 = this.service.getUser(params['id']);
      observable2.subscribe({
        next: data => {
          this.user = data;
        },
        error: error => {
          console.error('Error fetching user by id', error);
          this.notification.error('Gesuchte Benutzerseite kann nicht geladen werden.',"Fehler - Benutzerseite");
        }
      });
      let observable3 = this.service.getAllRecipesForUserId(params['id']);
      observable3.subscribe({
        next: data => {
          this.recipes = data;
        },
        error: error => {
          console.error('Error fetching recipes by user id', error);
          this.notification.error('Rezepte für die Benutzerseite können nicht abgerufen werden.',"Fehler - Benutzerseite Rezepte");
        }
      });
      this.service.getCurrentUser().subscribe({
        next: (data: userDto) => {
          this.isMyPage = (data.id == params['id']);
        },
        error: (error: any) => {
          console.error('Error fetching current User', error);
          this.notification.error('Eigene Benutzerseite kann nicht geladen werden.',"Fehler - Benutzerseite");
        }
      })

      this.ratingService.getRatingsByUserId(params['id']).subscribe({
        next: data => {
          this.ratings = data;
        },
        error: error => {
          console.error('Error fetching ratings by user id', error);
          this.notification.error('Bewertungen für die Benutzerseite können nicht abgerufen werden.',"Fehler - Benutzerseite Bewertungen");
        }
      });

    });

  }
}
