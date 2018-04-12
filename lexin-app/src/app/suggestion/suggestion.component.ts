import { Component, OnInit, Input, AfterContentInit, ElementRef } from '@angular/core';
import { DictItem, LangString } from '../domain';

@Component({
  selector: 'app-suggestion',
  template: `<div *ngFor="let iter of suggestion" class="suggestion-iter">
  {{iter.bok}}
  <span class="language">
    <img class="language-flag" src="assets/images/en.ico"> {{iter.eng}}</span>
</div>`,
  styleUrls: ['./suggestion.component.css']
})
export class SuggestionComponent implements AfterContentInit {
  @Input() suggestion: LangString[];
  constructor(private elementRef: ElementRef) {}
  ngAfterContentInit() {
    this.elementRef.nativeElement.classList.add('suggestion');
  }
}
