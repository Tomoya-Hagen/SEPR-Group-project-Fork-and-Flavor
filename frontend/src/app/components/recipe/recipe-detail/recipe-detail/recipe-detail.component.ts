import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RecipeDetailDto } from 'src/app/dtos/recipe';
import { RecipeService } from 'src/app/services/recipe.service';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.scss',
})
export class RecipeDetailComponent {
  recipe: RecipeDetailDto = {
    id: 0,
    rating: 0,
    name: "",
    description: "",
    numberOfServings: 0,
    forkedFromId: 0,
    ownerId: 0,
    categories: [],
    isDraft: false,
    recipeSteps: [],
    ingredients: [],
    allergens: [],
    nutritions: []
  };
  bannerError: string | null = null;
  slideConfig = {
    slidesToShow: 1,
    slidesToScroll: 1,
    dots: true
  }
  constructor(
    private service: RecipeService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let observable = this.service.getRecipeDetailsBy(params['id']);
      observable.subscribe({
        next: data => {
          this.recipe = data;
          console.log(this.recipe);

        },
        error: error => {
          console.error('Error fetching Horse', error);
          this.bannerError = 'Could not fetch horse: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Horse');
          this.router.navigate(['/horses'])
        }
      });
    });
  }
}
