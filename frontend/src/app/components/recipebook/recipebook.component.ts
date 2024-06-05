import {Component, OnDestroy, OnInit} from '@angular/core';
import { RecipeBookService } from "../../services/recipebook.service";
import { ToastrService } from "ngx-toastr";
import { NgForOf } from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import { FormsModule } from "@angular/forms";
import { CardComponent } from "../card/card.component";
import { RecipeBookListDto, RecipeBookSearch } from "../../dtos/recipe-book";
import {Subscription} from "rxjs";
import {NgbPagination} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-recipebook',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink,
    FormsModule,
    CardComponent,
    NgbPagination
  ],
  templateUrl: './recipebook.component.html',
  styleUrls: ['./recipebook.component.scss']
})
export class RecipebookComponent implements OnInit,OnDestroy {
  data: RecipeBookListDto[] = [];
  totalElements: number;
  page: number = 1;
  size: number = 24;
  pageSizes: number[] = [3, 9, 24, 90, 300, 600];
  RecipeBookSearch: RecipeBookSearch = {
    name: ''
  };
  pagedData: RecipeBookListDto[] = [];
  searchSubscription: Subscription;

  constructor(
    private service: RecipeBookService,
    private notification: ToastrService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.searchChanged();
  }

  ngOnDestroy(): void {
    if (this.searchSubscription) {
      this.searchSubscription.unsubscribe();
    }
  }

  searchChanged(): void {
    if (this.searchSubscription) {
      this.searchSubscription.unsubscribe();
    }
    this.searchSubscription = this.service.search(this.RecipeBookSearch.name, this.page - 1, this.size)
      .subscribe({
        next: (data: any) => {
          this.pagedData = data.content;
          this.totalElements = data.totalElements;
        },
        error: (error: any) => {
          console.error('Error fetching recipes', error);
          this.notification.error('Could not fetch recipes', 'Error');
        }
      });
  }

  onPageChange(pageNumber: number): void {
    this.page = pageNumber;
    this.searchChanged();
  }

  onPageSizeChange(event: Event): void {
    this.size = +(event.target as HTMLSelectElement).value;
    this.page = 1; // Reset to first page
    this.searchChanged();
  }

  detail(id: number): void {
    this.router.navigate(['/recipebook/details', id]);
  }
}
