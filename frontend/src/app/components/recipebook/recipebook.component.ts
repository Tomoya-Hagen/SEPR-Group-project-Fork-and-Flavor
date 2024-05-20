import {Component, OnInit} from '@angular/core';
import {RecipeBookService} from "../../services/recipebook.service";
import {ToastrService} from "ngx-toastr";
import {RecipeBookListDto} from "../../dtos/recipe-book";
import {NgForOf} from "@angular/common";
import { RouterLink} from "@angular/router";

@Component({
  selector: 'app-recipebook',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink
  ],
  templateUrl: './recipebook.component.html',
  styleUrl: './recipebook.component.scss'
})
export class RecipebookComponent implements OnInit{

  bannerError: string | null = null;
  data: RecipeBookListDto[] = []
  constructor(
    private service: RecipeBookService,
    private notification: ToastrService,
  ) {
  }

  ngOnInit(): void {
    console.log("Trying to get all recipe books.");
    this.service.getAllRecipeBooks().subscribe({
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

}
