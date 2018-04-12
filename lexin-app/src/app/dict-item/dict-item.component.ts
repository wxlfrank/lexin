import { Component, OnInit, Input } from '@angular/core';
import { DictItem } from '../domain';

@Component({
  selector: 'app-dict-item',
  templateUrl: './dict-item.component.html',
  styleUrls: ['./dict-item.component.css']
})
export class DictItemComponent implements OnInit {

  @Input() result: DictItem;
  constructor() { }

  ngOnInit() {
  }

}
