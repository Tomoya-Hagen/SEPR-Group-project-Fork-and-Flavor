import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WeekplanCreateComponent } from './weekplan-create.component';

describe('WeekplanCreateComponent', () => {
  let component: WeekplanCreateComponent;
  let fixture: ComponentFixture<WeekplanCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WeekplanCreateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WeekplanCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
