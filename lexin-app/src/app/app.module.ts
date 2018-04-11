import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { ConsoleComponent } from './console/console.component';
import { SearchComponent } from './search/search.component';
import { SuggestionComponent } from './suggestion/suggestion.component';


@NgModule({
  declarations: [
    AppComponent,
    ConsoleComponent,
    SearchComponent,
    SuggestionComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
