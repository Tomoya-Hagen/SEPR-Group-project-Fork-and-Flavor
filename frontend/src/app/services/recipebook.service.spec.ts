import { TestBed } from '@angular/core/testing';

import {RecipeBookService} from "./recipebook.service";

describe('RecipeService', () => {
  let service: RecipeBookService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecipeBookService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
