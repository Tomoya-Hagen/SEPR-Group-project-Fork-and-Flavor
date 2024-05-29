

export class Step {
  name: string;
  description?: string;
  recipeId?: number;
  whichstep: boolean;

  constructor() {
    this.name = "";
    this.description = "";
    this.recipeId = 0;
    this.whichstep = null;
  }

}
