import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RecipebookComponent} from "./components/recipebook/recipebook.component";
import {WeekplanComponent} from "./components/weekplan/weekplan.component";
import {RecipeComponent} from "./components/recipe/recipe.component";
import {RecipeCreateComponent} from "./components/recipe/recipe-create/recipe-create.component";
import { RecipebookCreateEditComponent, RecipeBookCreateEditMode } from './components/recipebook/recipebook-create-edit/recipebook-create-edit.component';
import {RegisterComponent} from "./components/register/register.component";
import { RecipeDetailComponent } from './components/recipe/recipe-detail/recipe-detail.component';
import {
  RecipebookDetailComponent
} from "./components/recipebook/recipe-book-detail/recipebook-detail/recipebook-detail.component";
import { RecipeEditComponent } from './components/recipe/recipe-edit/recipe-edit.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'recipe', component: RecipeComponent},
  {path: 'recipecreate', component: RecipeCreateComponent},
  {path: 'recipe', children: [
      {path: '', component: RecipeComponent},
      {path: 'details/:id', component: RecipeDetailComponent}
    ]},
  {path: 'recipebook', children: [
    {path: '', component: RecipebookComponent},
    {path: 'create', component: RecipebookCreateEditComponent, data: {mode: RecipeBookCreateEditMode.create}},
    {path: 'edit/:id', component: RecipebookCreateEditComponent, data: {mode: RecipeBookCreateEditMode.edit}},
    {path: '', component: RecipeComponent},
    {path: 'details/:id', component: RecipeDetailComponent},
    {path: 'edit/:id', component: RecipeEditComponent}
  ]},
  {path: 'recipebook', children:[
      {path: '', component: RecipebookComponent},
      {path: 'details/:id', component: RecipebookDetailComponent}
  ]},
  {path: 'weekplan', component: WeekplanComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
