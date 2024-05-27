import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {SlickCarouselModule} from "ngx-slick-carousel";
import {RecipeService} from "../../../../services/recipe.service";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {RecipeBookService} from "../../../../services/recipebook.service";
import {RecipeBookDetailDto} from "../../../../dtos/recipe-book";

@Component({
  selector: 'app-recipebook-detail',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    SlickCarouselModule,
    RouterLink
  ],
  templateUrl: './recipebook-detail.component.html',
  styleUrl: './recipebook-detail.component.scss'
})
export class RecipebookDetailComponent implements OnInit{
  bannerError: string | null = null;
  recipeBook: RecipeBookDetailDto = {
    name: "",
    description: "",
    id: 0,
    ownerId: 0,
    recipes: [],
    users: []
  }
  constructor(
    private service: RecipeBookService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let observable = this.service.getRecipeBookDetailsBy(params['id']);
      observable.subscribe({
        next: data => {
          this.recipeBook = data;
        },
        error: error => {
          console.error('Error fetching recipe books', error);
          this.bannerError = 'Could not fetch recipe books: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could not fetch recipe book');
        }
      });
    });
  }

}
