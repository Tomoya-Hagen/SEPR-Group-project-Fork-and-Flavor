import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {SlickCarouselModule} from "ngx-slick-carousel";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {RecipeBookService} from "../../../services/recipebook.service";
import {RecipeBookDetailDto} from "../../../dtos/recipe-book";
import { Title } from '@angular/platform-browser';
import {UserService} from "../../../services/user.service";

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
export class RecipebookDetailComponent implements OnInit, OnDestroy{
  recipeBook: RecipeBookDetailDto = {
    name: "",
    description: "",
    id: 0,
    ownerId: 0,
    owner: null,
    recipes: [],
    users: []
  }
  isOwner: boolean = false;

  constructor(
    private service: RecipeBookService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private titleService: Title,
    private userService: UserService,
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let observable = this.service.getRecipeBookDetailsBy(params['id']);
      observable.subscribe({
        next: data => {
          this.recipeBook = data;
          this.titleService.setTitle("Fork & Flavour | " + this.recipeBook.name);
          this.isCurrentUserOwner();
        },
        error: error => {
          console.error('Error fetching recipebook.', error);
          this.notification.error('Could not fetch recipebook.', 'Backend Error - Recipebook');
        }
      });
    });
  }

  ngOnDestroy() {
    this.titleService.setTitle("Fork & Flavour");
  }

  openUserPage() {
    this.router.navigate(['/userpage', this.recipeBook.ownerId]);
  }

  isCurrentUserOwner() {
    this.userService.getCurrentUser().subscribe(currentUser => {
      if (currentUser && this.recipeBook.ownerId === currentUser.id) {
        this.isOwner = true;
      }
    });
  }

  editRecipeBook() {
    this.router.navigate(['/recipebook/edit',this.recipeBook.id])
  }
}
