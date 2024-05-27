import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AutomaticCompleteComponent } from './automacomplete.component';

describe('AutocompleteComponent', () => {
  let component: AutomaticCompleteComponent;
  let fixture: ComponentFixture<AutomaticCompleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AutomaticCompleteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AutomaticCompleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
