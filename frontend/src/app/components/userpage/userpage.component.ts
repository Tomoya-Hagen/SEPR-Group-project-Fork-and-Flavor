import {Component, OnInit} from '@angular/core';
import {NgForOf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {RecipeBookDetailDto, RecipeBookListDto} from "../../dtos/recipe-book";
import {userDto} from "../../dtos/user";
import {RecipeListDto} from "../../dtos/recipe";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";
import {observable} from "rxjs";

@Component({
  selector: 'app-userpage',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink
  ],
  templateUrl: './userpage.component.html',
  styleUrl: './userpage.component.scss'
})
export class UserpageComponent implements OnInit {


  recipeBook: RecipeBookListDto[];
  recipes: RecipeListDto[] = [];
  bannerError: string | null = null;
  user: userDto;

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
          this.bannerError = 'Could not fetch recipe books by user id: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could not fetch recipe book by user id');
        }
      });
      let observable2 = this.service.getUser(params['id']);
      observable2.subscribe({
        next: data => {
          this.user = data;
        },
        error: error => {
          console.error('Error fetching user by id', error);
          this.bannerError = 'Could not fetch user by id: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could not fetch user by id');
        }
      });
    });

  }
}
