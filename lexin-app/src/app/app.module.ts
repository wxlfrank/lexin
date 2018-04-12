import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { ConsoleComponent } from './console/console.component';
import { SearchComponent } from './search/search.component';
import { SuggestionComponent } from './suggestion/suggestion.component';
import { DictItemComponent } from './dict-item/dict-item.component';


@NgModule({
  declarations: [
    AppComponent,
    ConsoleComponent,
    SearchComponent,
    SuggestionComponent,
    DictItemComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
