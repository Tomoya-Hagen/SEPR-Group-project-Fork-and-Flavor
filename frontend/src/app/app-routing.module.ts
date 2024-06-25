import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {ResetPasswordComponent} from './components/login/reset-password/reset-password.component';
import {RecipebookComponent} from "./components/recipebook/recipebook.component";
import {WeekplanComponent} from "./components/weekplan/weekplan.component";
import {RecipeComponent} from "./components/recipe/recipe.component";
import {RecipeCreateComponent} from "./components/recipe/recipe-create/recipe-create.component";
import { RecipebookCreateEditComponent, RecipeBookCreateEditMode } from './components/recipebook/recipebook-create-edit/recipebook-create-edit.component';
import {RegisterComponent} from "./components/register/register.component";
import { RecipeDetailComponent } from './components/recipe/recipe-detail/recipe-detail.component';
import { RecipebookDetailComponent } from "./components/recipebook/recipebook-detail/recipebook-detail.component";
import { RecipeEditComponent } from './components/recipe/recipe-edit/recipe-edit.component';
import {UserpageComponent} from "./components/userpage/userpage.component";
import {UserpageEditComponent} from "./components/userpage/userpage-edit/userpage-edit.component";
import { WeekplanCreateComponent } from './components/weekplan/weekplan-create/weekplan-create.component';
import {NotFoundComponent} from "./components/not-found/not-found.component";
import {DisclamerComponent} from "./components/disclamer/disclamer.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', children:[
      {path: '', component: LoginComponent},
      {path: 'reset', component: ResetPasswordComponent}
    ]},
  {path: 'register', component: RegisterComponent},
  {path: 'recipe', children: [
      {path: '', component: RecipeComponent},
      {path: 'details/:id', component: RecipeDetailComponent},
      {path: 'edit/:id', component: RecipeCreateComponent},
      {path: 'create', component: RecipeCreateComponent},
      {path: 'fork/:id', component: RecipeCreateComponent}
  ]},
  {path: 'recipebook', children:[
      {path: '', component: RecipebookComponent},
      {path: 'details/:id', component: RecipebookDetailComponent},
      {path: 'create', component: RecipebookCreateEditComponent, data: {mode: RecipeBookCreateEditMode.create}},
      {path: 'edit/:id', component: RecipebookCreateEditComponent, data: {mode: RecipeBookCreateEditMode.edit}}
  ]},
  {path: 'weekplan/:id', component: WeekplanComponent},
  {path: 'weekplan/:id/create',component:WeekplanCreateComponent},
  {path: 'userpage', children:[
      {path: ':id', component: UserpageComponent},
      {path: ':id/edit', component: UserpageEditComponent},
  ]},
  { path: 'disclamer', component: DisclamerComponent },
  { path: 'not-found', component: NotFoundComponent },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
