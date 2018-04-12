import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DictItemComponent } from './dict-item.component';

describe('DictItemComponent', () => {
  let component: DictItemComponent;
  let fixture: ComponentFixture<DictItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DictItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DictItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
