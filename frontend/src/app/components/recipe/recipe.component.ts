import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {debounceTime, Subject} from "rxjs";
import {RecipeList, RecipeSearch} from "../../dtos/recipe";
import {RecipeService} from "../../services/recipe.service";


const NAMES: RecipeList[] = [
  {id: 1, name: 'Apfelkuchen'},
  {id: 2, name: 'Becherkuchen'},
  {id: 3, name: 'Champions-Huhn'},
  {id: 4, name: 'Cookies'},
  {id: 5, name: 'Ei Spiegelei'},
  {id: 6, name: 'Eierlikör-Gugelhupf'},
  {id: 7, name: 'Gefüllte Zucchini'},
  {id: 8, name: 'Gemüse mit Huhn'},
  {id: 9, name: 'Griesbrei'},
  {id: 10, name: 'Faschiertes mit Gemüse'},
  {id: 11, name: 'Kürbiscremesuppe'},
  {id: 12, name: 'Käsekuchen'},
  {id: 13, name: 'Lachsnudeln'},
  {id: 14, name: 'Mais-Ripperl'},
  {id: 15, name: 'Nudeln mit Spinatsauce'},
  {id: 16, name: 'Pizzaschnecke'},
  {id: 17, name: 'Rahmkohlrabi'},
  {id: 18, name: 'Spaghetti Bolognese'},
  {id: 19, name: 'Steak nach Gordon Ramsay'},
  {id: 20, name: 'Zitronenkuchen'},
];

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrl: './recipe.component.scss'
})

export class RecipeComponent implements AfterViewInit, OnInit {
  displayedColumns: string[] = ['name'];
  dataSource: MatTableDataSource<RecipeList>;
  clickedRows = new Set<RecipeList>();

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  searchParams: RecipeSearch = new class implements RecipeSearch {
    name: string;
  };
  recipes: RecipeList[] = [];
  searchChangedObservable = new Subject<void>();

  constructor(
    private service: RecipeService,
  ) {
   // this.dataSource = new MatTableDataSource<RecipeList>(this.recipes);
    this.dataSource = new MatTableDataSource<RecipeList>(NAMES);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  ngOnInit(): void {
    this.reload();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({next: () => this.reload()});
  }

  reload() {
    this.service.search(this.searchParams)
      .subscribe({
        next: data => {
          this.recipes = data;
          this.ngAfterViewInit();
        }
      });
  }

  searchChanged(): void {
    this.searchChangedObservable.next();
  }
}

