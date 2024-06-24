import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {RecipeBookListDto} from "../../dtos/recipe-book";
import {userDto} from "../../dtos/user";
import {RecipeListDto} from "../../dtos/recipe";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";
import {RatingListDto} from "../../dtos/rating";
import {RatingService} from "../../services/rating.service";
import {FormsModule} from "@angular/forms";
import {Role} from "../../dtos/role";

@Component({
  selector: 'app-userpage',
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
  badges: Role[] = [];
  ratings: RatingListDto[] = [];
  menuOptions = [
    {
      label: 'Passwort ändern',
      action: () => this.changePassword()
    }
  ];

  constructor(
    private service: UserService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private ratingService: RatingService,
    private router: Router,
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let observable = this.service.getAllRecipeBooksForUserId(params['id']);
      this.service.getUser(params['id']).subscribe({
        next: data => {
          this.user = data;
          this.service.getCurrentUser().subscribe({
            next: (data: userDto) => {
              this.isMyPage = (data.id == params['id']);
            },
            error: (error: any) => {
              this.router.navigate(['not-found']);
            }
          });
          observable.subscribe({
            next: data => {
              this.recipeBook = data;
            },
            error: error => {
              console.error('Error fetching recipe books by user id', error);
              this.notification.error('Rezeptbücher für die Benutzerseite können nicht abgerufen werden.',"Fehler - Benutzerseite Rezeptbücher");
            }
          });
          this.ratingService.getRatingsByUserId(params['id']).subscribe({
            next: data => {
              this.ratings = data;
            },
            error: error => {
              console.error('Error fetching ratings by user id', error);
              this.notification.error('Bewertungen für die Benutzerseite können nicht abgerufen werden.',"Fehler - Benutzerseite Bewertungen");
            }
          });
          this.service.getAllRecipesForUserId(params['id']).subscribe({
            next: data => {
              this.recipes = data;
            },
            error: error => {
              console.error('Error fetching recipes by user id', error);
              this.notification.error('Rezepte für die Benutzerseite können nicht abgerufen werden.',"Fehler - Benutzerseite Rezepte");
            }
          });
        },
        error: error => {
          this.router.navigate(['not-found']);
        }
      });
      this.service.getBadgesOfUser(params['id']).subscribe({
        next: data => {
          this.badges = data;
        },
        error: error => {
          console.error('Error fetching badges by user id', error);
          this.notification.error('Badges für die Benutzerseite können nicht abgerufen werden.', "Backend Fehler - Benutzerseite Rezepte");
        }
      });

    });
  }

  changePassword() {
    this.router.navigate(['/userpage', this.user.id, 'edit']);
  }

  goToRecipeDetails(recipeId: number): void {
    this.router.navigate([`/recipe/details/${recipeId}`]);
  }
}
