import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';

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

export class RecipeComponent implements AfterViewInit {
  displayedColumns: string[] = ['name'];
  dataSource: MatTableDataSource<RecipeData>;
  clickedRows = new Set<RecipeData>();

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor() {
    const recipes = Array.from({length: 20}, (_, k) => createNewRecipe(k + 1));
    this.dataSource = new MatTableDataSource(recipes);
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
