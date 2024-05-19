import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {RecipebookComponent} from "./components/recipebook/recipebook.component";
import {WeekplanComponent} from "./components/weekplan/weekplan.component";
import {RecipeComponent} from "./components/recipe/recipe.component";
import { RecipebookCreateEditComponent, RecipeBookCreateEditMode } from './components/recipebook/recipebook-create-edit/recipebook-create-edit.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', canActivate: mapToCanActivate([AuthGuard]), component: MessageComponent},
  {path: 'recipe', component: RecipeComponent},
  {path: 'recipebook', children: [
    {path: '', component: RecipebookComponent},
    {path: 'create', component: RecipebookCreateEditComponent, data: {mode: RecipeBookCreateEditMode.create}},
    {path: 'edit/:id', component: RecipebookCreateEditComponent, data: {mode: RecipeBookCreateEditMode.edit}},
  ]},
  {path: 'weekplan', component: WeekplanComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
