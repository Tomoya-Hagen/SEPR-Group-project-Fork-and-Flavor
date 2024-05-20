import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {RecipebookComponent} from "./components/recipebook/recipebook.component";
import {WeekplanComponent} from "./components/weekplan/weekplan.component";
import {RecipeComponent} from "./components/recipe/recipe.component";
import {RegisterComponent} from "./components/register/register.component";
import { RecipeDetailComponent } from './components/recipe/recipe-detail/recipe-detail/recipe-detail.component';
import {
  RecipebookDetailComponent
} from "./components/recipebook/recipe-book-detail/recipebook-detail/recipebook-detail.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'message', canActivate: mapToCanActivate([AuthGuard]), component: MessageComponent},
  {path: 'recipe', children: [
    {path: '', component: RecipeComponent},
    {path: 'details/:id', component: RecipeDetailComponent}
  ]},
  {path: 'recipebook', children:[
      {path: '', component: RecipebookComponent},
      {path: ':id/details', component: RecipebookDetailComponent}
    ]},
  {path: 'weekplan', component: WeekplanComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
