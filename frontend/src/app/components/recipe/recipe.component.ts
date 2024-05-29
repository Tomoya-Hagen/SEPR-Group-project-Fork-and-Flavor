import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {RecipeListDto, RecipeSearch} from "../../dtos/recipe";
import {RecipeService} from "../../services/recipe.service";
import { Title } from '@angular/platform-browser';

import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrl: './recipe.component.scss'
})

export class RecipeComponent implements OnInit, OnDestroy {
  bannerError: string | null = null;
  data: RecipeListDto[] = [];
  recipeSearch: RecipeSearch = new class implements RecipeSearch {
    name: string;
  };
  page =1;
  step = 50; //all recipe

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private service: RecipeService,
    private notification: ToastrService,
    private router: Router,
    private titleService: Title,
  ) {
  }

  ngOnInit(): void {
    this.loadPage()
    this.titleService.setTitle("Fork & Flavour | Alle Rezepte");
  }

  ngOnDestroy() {
    this.titleService.setTitle("Fork & Flavour");
  }

  loadPage() {
    console.log("Trying to get all recipe books.");
    this.service.getListByPageAndStep(this.page, this.step).subscribe({
      next: items => {
        console.log("Successfully received all recipe books");
        this.data = items;
      },
      error: error => {
        console.error('Error fetching recipes', error);
        this.bannerError = 'Could not fetch recipes: ' + error.message;
        const errorMessage = error.status === 0
          ? 'Is the backend up?'
          : error.message.message;
        this.notification.error(errorMessage, 'Could Not Fetch Recipes');
      }})
  }

  searchChanged(): void {
    console.log("Trying to get the searched recipe.");
    this.service.search(this.recipeSearch).subscribe({
      next: items => {
        console.log("Successfully received all recipe books");
        this.data = items;
      },
      error: error => {
        console.error('Error fetching recipes', error);
        this.bannerError = 'Could not fetch recipe books: ' + error.message;
        const errorMessage = error.status === 0
          ? 'Is the backend up?'
          : error.message.message;
        this.notification.error(errorMessage, 'Could Not Fetch Recipes');
      }})
  }

  detail(id:number){
    this.router.navigate(['/recipe/details', id]);
  }
}
