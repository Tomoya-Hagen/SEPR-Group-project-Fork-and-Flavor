import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {debounceTime, Subject} from "rxjs";
import {RecipeList, RecipeSearch} from "../../dtos/recipe";
import {RecipeService} from "../../services/recipe.service";

export interface RecipeData{
  id: string;
  name: string;
}

const NAMES: string[] = [
  'Apfelkuchen',
  'Pizzaschnecke',
  'Kürbisccremesuppe',
  'Kaiserschmarrn', 'Schnitzel',
  'Spagthetti Bolognese',
  'Becherkuchen',
  'Bratkartoffel',
  'Eierreis',
  'Grillgemüse',
  'Gefüllte Zucchini',
  'Kartoffelpüree',
  'Joghurtgugelhupf',
  'Hühnerbrust gefüllt',
  'Käsekuchen',
];

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrl: './recipe.component.scss'
})

export class RecipeComponent implements AfterViewInit, OnInit {
  displayedColumns: string[] = ['name'];
  dataSource: MatTableDataSource<RecipeData>;
  clickedRows = new Set<RecipeData>();

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  searchParams: RecipeSearch = {};
  recipes: RecipeList[] = [];
  searchChangedObservable = new Subject<void>();

  constructor(
    private service: RecipeService,
  ) {
    const recipesExample = Array.from({length: 15}, (_, k) => createNewRecipe(k + 1));
    this.dataSource = new MatTableDataSource(recipesExample);

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


/** Builds and returns a new User. */
function createNewRecipe(id: number): RecipeData {
  const name =
    NAMES[Math.round(Math.random() * (NAMES.length - 1))] +
    ' ' ;

  return {
    id: id.toString(),
    name: name,
  };
}
