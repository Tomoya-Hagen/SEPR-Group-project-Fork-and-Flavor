import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import { RecipebookComponent } from './components/recipebook/recipebook.component';
import { RecipebookCreateEditComponent } from './components/recipebook/recipebook-create-edit/recipebook-create-edit.component';
import { RecipeComponent } from './components/recipe/recipe.component';
import { RecipeDetailComponent } from './components/recipe/recipe-detail/recipe-detail.component';
import {RecipeCreateComponent} from "./components/recipe/recipe-create/recipe-create.component";
import { ToastrModule } from 'ngx-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { SlickCarouselModule } from 'ngx-slick-carousel';
import {RegisterComponent} from "./components/register/register.component";
import { RecipeEditComponent } from './components/recipe/recipe-edit/recipe-edit.component';
import { AutocompleteComponent } from './components/autocomplete/autocomplete.component';
import { IngredientComponent } from './components/recipe/recipe-create/ingredient/ingredient.component';
import { AutomaticCompleteComponent } from './components/automacomplete/automacomplete.component'
import {CardComponent} from "./components/card/card.component";

@NgModule({
  declarations: [
    AppComponent,
    AutocompleteComponent,
    AutomaticCompleteComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    IngredientComponent,
    AutocompleteComponent,
    RecipeCreateComponent,
    IngredientComponent,
    RecipeDetailComponent,
    RecipeEditComponent,
    RegisterComponent,
    AutocompleteComponent,
    RegisterComponent,
    RecipeComponent,
    RecipebookCreateEditComponent,

  ],
  imports: [
    BrowserModule,
    RecipebookComponent,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    ToastrModule.forRoot(),
    // Needed for Toastr
    BrowserAnimationsModule,
    SlickCarouselModule,
    CardComponent,
  ],
  providers: [httpInterceptorProviders],
  exports: [
    AutocompleteComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
