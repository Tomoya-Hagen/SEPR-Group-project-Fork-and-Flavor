import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-recipe-step',
  templateUrl: './recipe-step.component.html',
  styleUrls: ['./recipe-step.component.scss']
})
export class RecipeStepComponent {
  @Input() step: any;
  expanded: boolean = false;

  toggleStep() {
    if (this.step.recipe) {
      this.expanded = !this.expanded;
    }
  }

  navigateToDetailsInNewTab(index: number) {
    const baseUrl = window.location.origin;
    const url = `${baseUrl}/#/recipe/details/${index}`;
    window.open(url, '_blank');
  }
}
