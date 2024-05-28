import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {debounceTime, Subject} from "rxjs";
import {RecipeList, RecipeSearch} from "../../dtos/recipe";
import {RecipeService} from "../../services/recipe.service";
import {RecipeBookSearch} from "../../dtos/recipe-book";


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

const NAMES: RecipeList[] = [
  {id: 1, name: 'Apfelkuchen'},
  {id: 2, name: 'Bananenkuchen'},
  {id: 3, name: 'Becherkuchen'},
  {id: 4, name: 'Beef und Brokkoli Soja'},
  {id: 5, name: 'Bratkartoffel'},
  {id: 6, name: 'Braunhirse Erdmandel Schnitten'},
  {id: 7, name: 'Brokkoli Strude'},
  {id: 8, name: 'Brokkoli-Lauch-Nudeln'},
  {id: 9, name: 'Brokkoli-Nudeln'},
  {id: 10, name: 'Cake Pops'},
  {id: 11, name: 'Champion Spaghetti'},
  {id: 12, name: 'Champions-Huhn'},
  {id: 13, name: 'Cookies'},
  {id: 14, name: 'Ei knusprig mit Olivenöl'},
  {id: 15, name: 'Ei scrambled'},
  {id: 16, name: 'Ei Spiegelei'},
  {id: 17, name: 'Eieraufstrich'},
  {id: 18, name: 'Eierlikör-Gugelhupf'},
  {id: 19, name: 'ErbsenChampingnon Risotto'},
  {id: 20, name: 'Erdnuss Chili Nudeln'},
  {id: 21, name: 'Erdäpfel asiatisch'},
  {id: 22, name: 'Erdäpfel griechisch'},
  {id: 23, name: 'Erdäpfel Lauch Suppe'},
  {id: 24, name: 'Faschiertes mit Gemüse'},
  {id: 25, name: 'Feta Nudeln'},
  {id: 26, name: 'Gebrannte Mandeln'},
  {id: 27, name: 'Gefüllte Zucchini'},
  {id: 28, name: 'Gemüse mit Huhn'},
  {id: 29, name: 'Gratin'},
  {id: 30, name: 'Griechische Erbsen nach HE'},
  {id: 31, name: 'Griesbrei'},
  {id: 32, name: 'Grillgemüse'},
  {id: 33, name: 'Grillhuhn'},
  {id: 34, name: 'Guacamole'},
  {id: 35, name: 'Hirse mit Tomaten und Speck'},
  {id: 36, name: 'Hirseauflauf mit Gemüse'},
  {id: 37, name: 'Hühnerbrust gefüllt'},
  {id: 38, name: 'Hähnchenbruststreifen im Kräutermantel'},
  {id: 39, name: 'Joghurtgugelhupf'},
  {id: 40, name: 'Kaiserschmarren'},
  {id: 11, name: 'Kürbiscremesuppe'},
  {id: 12, name: 'Käsekuchen'},
  {id: 13, name: 'Lachsnudeln'},
  {id: 14, name: 'Mais-Ripperl'},
  {id: 15, name: 'Nudeln mit Spinatsauce'},
  {id: 16, name: 'Pizzaschnecke'},
  {id: 17, name: 'Rahmkohlrabi'},
  {id: 18, name: 'Spaghetti Bolognese'},
  {id: 19, name: 'Steak nach Gordon Ramsay'},
  {id: 20, name: 'Zitronenkuchen'}]


