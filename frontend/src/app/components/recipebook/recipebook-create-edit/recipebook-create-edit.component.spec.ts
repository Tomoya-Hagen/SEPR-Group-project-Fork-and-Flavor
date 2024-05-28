import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipebookCreateEditComponent } from './recipebook-create-edit.component';

describe('RecipebookCreateEditComponent', () => {
  let component: RecipebookCreateEditComponent;
  let fixture: ComponentFixture<RecipebookCreateEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipebookCreateEditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RecipebookCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
