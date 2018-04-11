import { Component, OnInit } from '@angular/core';
import { DictItem, LangString } from '../domain';

@Component({
  selector: 'app-suggestion',
  templateUrl: './suggestion.component.html',
  styleUrls: ['./suggestion.component.css']
})
export class SuggestionComponent implements OnInit {
  items: DictItem[];
  suggestions: LangString[];
  constructor() {}

  ngOnInit() {
    this.items = [
      {
        clazz: 'verb',
        examples:
          'Bokmål det kan hende at alt går bra\nEngelsk everything may, of course, turn out perfectly ' +
          'well\nBokmål hva hendte med søstera di?\nEngelsk what happened to your sister?\n' +
          'Bokmål det hendte på 60-tallet\nEngelsk it happened in the sixties',
        explain: 'Bokmål skje , inntreffe, forekomme',
        format: '[hender hendte hendt]',
        sound: '/hene/',
        syllabel: 'hende',
        word: 'hende\nEngelsk happen, occur, take place\nverb'
      },
      {
        clazz: 'substantiv',
        composite:
          'Bokmål læreplan\nEngelsk curriculum\nBokmål framtidsplan\nEngelsk plan for the future',
        examples:
          'Bokmål en plan for utbygging\nEngelsk an extension plan\nBokmål realisere ' +
          'planene sine\nEngelsk put one\'s plans into effect\nBokmål ha planer om (å gjøre) noe\n' +
          'Engelsk have plans for (to do) something\nBokmål alt går etter planen (\'alt går som planlagt\')\n' +
          'Engelsk everything is going according to plan (\'everything is going as planned)',
        explain:
          'Bokmål et forslag til hvordan noe skal gjøres (for å nå et mål)',
        format: '[planen planer planene]',
        sound: '/pla:n/',
        syllabel: 'plan',
        word: 'plan\nEngelsk plan\nnoun'
      },
      {
        clazz: 'substantiv',
        composite:
          'Bokmål plantegnng\nEngelsk plan drawing\nBokmål byggetegning\n' +
          'Engelsk construction (working, structural) drawing (design)',
        examples:
          'Bokmål tegninger til en ny hustype\nEngelsk plans (drawings) for a new type of house',
        explain: 'Bokmål en plan (for konstruksjon av hus, maskiner e l)',
        format: '[tegningen (el tegninga) tegninger tegningene]',
        phrases:
          'Bokmål (alt går) etter tegningen (\'(alt går) som planlagt\')\n' +
          'Engelsk (everything is going) according to plan (\'as planned)',
        sound: '/tæining/',
        syllabel: 'tegning',
        word: 'tegning\nEngelsk drawing, plan, blueprint\nnoun'
      },
      {
        examples:
          'Bokmål (alt går) etter tegningen\nEngelsk (everything is going) according to plan (\'as planned)',
        explain: 'Bokmål (alt går) som planlagt\nEngelsk ',
        word: 'etter tegningen\nEngelsk  according to plan (\'as planned)'
      }
    ];
    const result = [];
    for (const item of this.items) {
      const word: string = item.word;
      console.log(word);
      const bok: string[] = word.split('Bokmål ');
      console.log(bok);
      for (const str of bok) {
        const final: string[] = str.split('Engelsk ');
        console.log(final);
        const langStr = new LangString();
        langStr.bok = final[0];
        if (final.length > 1) {
          langStr.eng = final[1];
        }
        result.push(langStr);
      }
    }
    this.suggestions = result;
  }
}
