import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {RecipeBookService} from "../../../services/recipebook.service";
import {RecipeBookDetailDto} from "../../../dtos/recipe-book";
import { Title } from '@angular/platform-browser';
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-recipebook-detail',
  templateUrl: './recipebook-detail.component.html',
  styleUrls: ['./recipebook-detail.component.scss']
})
export class RecipebookDetailComponent implements OnInit, OnDestroy {
  recipeBook: RecipeBookDetailDto = {
    name: "",
    description: "",
    id: 0,
    ownerId: 0,
    owner: {
      id: 0,
      name: ""
    },
    recipes: [],
    users: []
  }
  canEdit: boolean = false;
  menuOptions = [
    {
      label: 'Neues Rezeptbuch erstellen',
      action: () => this.newRecipeBook(),
      disabled: false
    },
    {
      label: 'Rezeptbuch bearbeiten',
      action: () => this.editRecipeBook(),
      disabled: true
    },
    {
      label: 'Wochenplan',
      action: () => this.gotoWeekPlan(),
      disabled: false
    }
  ];

  constructor(
    private service: RecipeBookService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private titleService: Title,
    private userService: UserService,
  ) {}

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
          this.router.navigate(['not-found']);
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
        this.canEdit = true;
        this.updateMenuOptions();
        console.log("User is owner");
        return;
      }

      for (let i = 0; i < this.recipeBook.users.length; i++) {
        if (currentUser && this.recipeBook.users[i].id === currentUser.id) {
          this.canEdit = true;
          this.updateMenuOptions();
          console.log("User is in recipebook");
          return;
        }
      }
    });
  }

  updateMenuOptions() {
    this.menuOptions = [
      {
        label: 'Neues Rezeptbuch erstellen',
        action: () => this.newRecipeBook(),
        disabled: false
      },
      {
        label: 'Rezeptbuch bearbeiten',
        action: () => this.editRecipeBook(),
        disabled: !this.canEdit
      },
      {
        label: 'Wochenplan',
        action: () => this.gotoWeekPlan(),
        disabled: false
      }
    ];
  }

  editRecipeBook() {
    this.router.navigate(['/recipebook/edit', this.recipeBook.id]);
  }

  newRecipeBook() {
    this.router.navigate(['/recipebook/create']);
  }
  gotoWeekPlan(){
    this.router.navigate(['/weekplan/'+ this.recipeBook.id])
  }
}
