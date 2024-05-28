// card.component.ts
import { Component, Input } from '@angular/core';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  standalone: true,
  imports: [
    RouterLink
  ]
})
export class CardComponent {
  @Input() item: any;
  @Input() detailUrlPrefix: string = '/details';
}
