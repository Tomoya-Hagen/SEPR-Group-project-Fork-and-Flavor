import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipebookDetailComponent } from './recipebook-detail.component';

describe('RecipebookDetailComponent', () => {
  let component: RecipebookDetailComponent;
  let fixture: ComponentFixture<RecipebookDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipebookDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RecipebookDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
