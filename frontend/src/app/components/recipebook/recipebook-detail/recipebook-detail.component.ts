import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {SlickCarouselModule} from "ngx-slick-carousel";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {RecipeBookService} from "../../../services/recipebook.service";
import {RecipeBookDetailDto} from "../../../dtos/recipe-book";
import { Title } from '@angular/platform-browser';

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
  constructor(
    private service: RecipeBookService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private titleService: Title,
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let observable = this.service.getRecipeBookDetailsBy(params['id']);
      observable.subscribe({
        next: data => {
          this.recipeBook = data;
          this.titleService.setTitle("Fork & Flavour | " + this.recipeBook.name);
        },
        error: error => {
          console.error('Error fetching recipebook.', error);
          this.notification.error('Rezeptbücher können nicht abgerufen werden.',"Backend Fehler - Rezeptbuch");
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
}
